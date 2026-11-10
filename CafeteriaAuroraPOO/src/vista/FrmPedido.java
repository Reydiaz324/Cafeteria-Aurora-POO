package vista;

import controlador.GestorCafeteria;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import modelo.Cliente;
import modelo.ComprobantePago;
import modelo.Empleado;
import modelo.Pedido;
import modelo.Producto;

/**
 * FORMULARIO: FrmPedido
 * Pantalla de atención y Punto de Venta (POS) con soporte visual en NetBeans (.form).
 */
public class FrmPedido extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private GestorCafeteria gestor;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloCarrito;
    private final List<ItemCarritoVisual> itemsCarrito = new ArrayList<>();
    private double totalCalculado = 0.0;

    private static class ItemCarritoVisual {
        Producto producto;
        int cantidad;
        double subtotal;

        ItemCarritoVisual(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.subtotal = producto.getPrecio() * cantidad;
        }
    }

    public FrmPedido() {
        this(new GestorCafeteria());
    }

    public FrmPedido(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        personalizarComponentes();
        cargarEmpleados();
        cargarProductos(0);
        this.setSize(1100, 700);
        this.setLocationRelativeTo(null);
    }

    private void personalizarComponentes() {
        modeloProductos = new DefaultTableModel(new String[]{"ID", "Producto", "Precio (S/)", "Stock Disp."}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblProductos.setModel(modeloProductos);

        modeloCarrito = new DefaultTableModel(new String[]{"ID", "Producto", "Precio", "Cant.", "Subtotal (S/)"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblCarrito.setModel(modeloCarrito);

        // DocumentFilter: 8 dígitos numéricos exactos
        ((AbstractDocument) txtDni.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder();
                for (char c : string.toCharArray()) {
                    if (Character.isDigit(c)) sb.append(c);
                }
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                int allowed = 8 - current.length();
                if (allowed > 0) {
                    String toInsert = sb.length() > allowed ? sb.substring(0, allowed) : sb.toString();
                    super.insertString(fb, offset, toInsert, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    if (Character.isDigit(c)) sb.append(c);
                }
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                int allowed = 8 - (current.length() - length);
                if (allowed > 0) {
                    String toReplace = sb.length() > allowed ? sb.substring(0, allowed) : sb.toString();
                    super.replace(fb, offset, length, toReplace, attrs);
                }
            }
        });
    }

    private void cargarEmpleados() {
        cboEmpleado.removeAllItems();
        if (gestor != null && gestor.getEmpleados() != null) {
            for (Empleado e : gestor.getEmpleados()) {
                cboEmpleado.addItem(e.getNombreCompleto() + " (" + e.getCargo() + ")");
            }
        }
    }

    private void cargarProductos(int idCat) {
        modeloProductos.setRowCount(0);
        if (gestor == null) return;
        List<Producto> lista = gestor.filtrarProductos(idCat, "");
        for (Producto p : lista) {
            modeloProductos.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                String.format("%.2f", p.getPrecio()),
                p.getStock()
            });
        }
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        totalCalculado = 0.0;
        for (ItemCarritoVisual it : itemsCarrito) {
            totalCalculado += it.subtotal;
            modeloCarrito.addRow(new Object[]{
                it.producto.getIdProducto(),
                it.producto.getNombreProducto(),
                String.format("S/ %.2f", it.producto.getPrecio()),
                it.cantidad,
                String.format("S/ %.2f", it.subtotal)
            });
        }
        lblTotal.setText(String.format("S/ %.2f", totalCalculado));
    }

    private void agregarProductoSeleccionado() {
        int fila = tblProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del catálogo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idProd = (int) modeloProductos.getValueAt(fila, 0);
        Producto prod = gestor.buscarProductoPorId(idProd);
        if (prod == null) return;

        if (prod.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "El producto '" + prod.getNombreProducto() + "' no cuenta con stock disponible.", "Sin Stock", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ItemCarritoVisual existente = null;
        for (ItemCarritoVisual it : itemsCarrito) {
            if (it.producto.getIdProducto() == prod.getIdProducto()) {
                existente = it;
                break;
            }
        }

        if (existente != null) {
            if (existente.cantidad + 1 > prod.getStock()) {
                JOptionPane.showMessageDialog(this, "No puede agregar más unidades de las disponibles en inventario (" + prod.getStock() + ").", "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            existente.cantidad++;
            existente.subtotal = existente.cantidad * prod.getPrecio();
        } else {
            itemsCarrito.add(new ItemCarritoVisual(prod, 1));
        }
        actualizarTablaCarrito();
    }

    private void quitarProductoSeleccionado() {
        int fila = tblCarrito.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del carrito a quitar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        itemsCarrito.remove(fila);
        actualizarTablaCarrito();
    }

    private void limpiarFormulario() {
        itemsCarrito.clear();
        actualizarTablaCarrito();
        txtDni.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
    }

    private void finalizarPedido() {
        if (itemsCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto al pedido.", "Pedido vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String doc = txtDni.getText().trim();
        if (doc.length() != 8 || !doc.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "El DNI del cliente es obligatorio y debe tener exactamente 8 dígitos numéricos.", "DNI Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtDni.requestFocus();
            return;
        }
        String nombres = txtNombre.getText().trim();
        String apellidos = txtApellido.getText().trim();
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del cliente.", "Nombre Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if (apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el apellido del cliente.", "Apellido Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtApellido.requestFocus();
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this, "¿Desea finalizar este pedido y emitir Boleta de Venta?", "Confirmar Pedido", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            int idxEmp = cboEmpleado.getSelectedIndex();
            Empleado empleado = (idxEmp >= 0 && idxEmp < gestor.getEmpleados().size())
                    ? gestor.getEmpleados().get(idxEmp)
                    : gestor.getEmpleados().get(0);

            Cliente cliente = new Cliente(gestor.getClientes().size() + 1, nombres, apellidos, doc, "987654321", "cliente@aurora.pe");
            int nuevoId = gestor.getPedidos().size() + 1;
            Pedido pedido = new Pedido(nuevoId, cliente, empleado, (String) cboMetodoPago.getSelectedItem(), "En local");

            for (ItemCarritoVisual it : itemsCarrito) {
                pedido.agregarItem(it.producto, it.cantidad, "Regular", "Caliente", "Ninguno");
            }

            ComprobantePago comprobante = gestor.registrarPedidoYComprobante(pedido, "BOLETA", doc, "", "");
            String msg = String.format("¡Boleta N. %s registrada exitosamente!\n\nCliente: %s %s\nDNI: %s\nTotal Pagado: S/ %.2f\nTicket: comprobantes/%s.txt",
                    comprobante.getNumeroSerie(), cliente.getNombres(), cliente.getApellidos(), doc, comprobante.calcularTotal(), comprobante.getNumeroSerie());
            JOptionPane.showMessageDialog(this, msg, "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarProductos(cboCategoria.getSelectedIndex());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verComprobante() {
        if (itemsCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El pedido está vacío.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String doc = txtDni.getText().trim();
        String nombres = txtNombre.getText().trim();
        String apellidos = txtApellido.getText().trim();
        Cliente cliente = new Cliente(0, nombres.isEmpty() ? "Cliente" : nombres, apellidos.isEmpty() ? "General" : apellidos, doc.isEmpty() ? "00000000" : doc, "", "");

        int idxEmp = cboEmpleado.getSelectedIndex();
        Empleado emp = (idxEmp >= 0 && idxEmp < gestor.getEmpleados().size()) ? gestor.getEmpleados().get(idxEmp) : gestor.getEmpleados().get(0);

        try {
            Pedido pedTemp = new Pedido(0, cliente, emp, (String) cboMetodoPago.getSelectedItem(), "En local");
            for (ItemCarritoVisual it : itemsCarrito) {
                pedTemp.agregarItem(it.producto, it.cantidad, "Regular", "Caliente", "Ninguno");
            }
            modelo.Boleta boletaTemp = new modelo.Boleta("B001-PREVIEW", "HOY", pedTemp, doc.isEmpty() ? "00000000" : doc);

            JTextArea txt = new JTextArea(boletaTemp.generarContenidoImprimible(), 20, 42);
            txt.setFont(new Font("Monospaced", Font.PLAIN, 12));
            txt.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(txt), "Previsualización de Boleta", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar vista previa: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSuperior = new javax.swing.JPanel();
        lblDni = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblApellido = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        lblEmpleado = new javax.swing.JLabel();
        cboEmpleado = new javax.swing.JComboBox<>();
        pnlCentro = new javax.swing.JPanel();
        pnlProductos = new javax.swing.JPanel();
        pnlFiltro = new javax.swing.JPanel();
        lblFiltro = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        btnAgregarProducto = new javax.swing.JButton();
        pnlCarrito = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        pnlAccionesCarrito = new javax.swing.JPanel();
        btnQuitar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        pnlInferior = new javax.swing.JPanel();
        pnlPago = new javax.swing.JPanel();
        lblMetodoPago = new javax.swing.JLabel();
        cboMetodoPago = new javax.swing.JComboBox<>();
        lblTotalTexto = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        pnlBotonesAccion = new javax.swing.JPanel();
        btnVerComprobante = new javax.swing.JButton();
        btnFinalizarPedido = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Atención de Pedidos - Cafetería Aurora");
        setBackground(new java.awt.Color(245, 237, 224));

        pnlSuperior.setBackground(new java.awt.Color(76, 47, 29));
        pnlSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pnlSuperior.setLayout(new java.awt.GridLayout(2, 4, 10, 8));

        lblDni.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDni.setForeground(new java.awt.Color(255, 255, 255));
        lblDni.setText("DNI Cliente (8 dígitos):");
        pnlSuperior.add(lblDni);

        txtDni.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        pnlSuperior.add(txtDni);

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombres:");
        pnlSuperior.add(lblNombre);

        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        pnlSuperior.add(txtNombre);

        lblApellido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 255, 255));
        lblApellido.setText("Apellidos:");
        pnlSuperior.add(lblApellido);

        txtApellido.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        pnlSuperior.add(txtApellido);

        lblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblEmpleado.setText("Atendido por:");
        pnlSuperior.add(lblEmpleado);

        cboEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        pnlSuperior.add(cboEmpleado);

        getContentPane().add(pnlSuperior, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(245, 237, 224));
        pnlCentro.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pnlCentro.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        pnlProductos.setBackground(new java.awt.Color(245, 237, 224));
        pnlProductos.setBorder(javax.swing.BorderFactory.createTitledBorder("Catálogo de Productos"));
        pnlProductos.setLayout(new java.awt.BorderLayout(0, 6));

        pnlFiltro.setBackground(new java.awt.Color(245, 237, 224));
        pnlFiltro.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFiltro.setText("Categoría:");
        pnlFiltro.add(lblFiltro);

        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Todas las Categorías --", "Lattes / Cappuccino", "Frappuccinos", "Panadería / Empanadas", "Postres / Tortas" }));
        cboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriaActionPerformed(evt);
            }
        });
        pnlFiltro.add(cboCategoria);

        pnlProductos.add(pnlFiltro, java.awt.BorderLayout.NORTH);

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Precio (S/)", "Stock"
            }
        ) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductos.setRowHeight(24);
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductos);

        pnlProductos.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnAgregarProducto.setBackground(new java.awt.Color(140, 90, 50));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar Producto al Pedido");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        pnlProductos.add(btnAgregarProducto, java.awt.BorderLayout.SOUTH);

        pnlCentro.add(pnlProductos);

        pnlCarrito.setBackground(new java.awt.Color(245, 237, 224));
        pnlCarrito.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle del Pedido Actual (Carrito)"));
        pnlCarrito.setLayout(new java.awt.BorderLayout(0, 6));

        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Precio", "Cant.", "Subtotal (S/)"
            }
        ) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCarrito.setRowHeight(24);
        jScrollPane2.setViewportView(tblCarrito);

        pnlCarrito.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnlAccionesCarrito.setBackground(new java.awt.Color(245, 237, 224));
        pnlAccionesCarrito.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnQuitar.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnQuitar.setText("Quitar Producto");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });
        pnlAccionesCarrito.add(btnQuitar);

        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        pnlAccionesCarrito.add(btnLimpiar);

        pnlCarrito.add(pnlAccionesCarrito, java.awt.BorderLayout.SOUTH);

        pnlCentro.add(pnlCarrito);

        getContentPane().add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlInferior.setBackground(new java.awt.Color(76, 47, 29));
        pnlInferior.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        pnlInferior.setLayout(new java.awt.BorderLayout());

        pnlPago.setOpaque(false);
        pnlPago.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        lblMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblMetodoPago.setForeground(new java.awt.Color(255, 255, 255));
        lblMetodoPago.setText("Método de Pago:");
        pnlPago.add(lblMetodoPago);

        cboMetodoPago.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cboMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta", "Yape", "Plin" }));
        pnlPago.add(cboMetodoPago);

        lblTotalTexto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalTexto.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalTexto.setText("   TOTAL: ");
        pnlPago.add(lblTotalTexto);

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(232, 195, 158));
        lblTotal.setText("S/ 0.00");
        pnlPago.add(lblTotal);

        pnlInferior.add(pnlPago, java.awt.BorderLayout.WEST);

        pnlBotonesAccion.setOpaque(false);
        pnlBotonesAccion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btnVerComprobante.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnVerComprobante.setText("Ver Comprobante");
        btnVerComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerComprobanteActionPerformed(evt);
            }
        });
        pnlBotonesAccion.add(btnVerComprobante);

        btnFinalizarPedido.setBackground(new java.awt.Color(140, 90, 50));
        btnFinalizarPedido.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnFinalizarPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalizarPedido.setText("Finalizar Pedido");
        btnFinalizarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarPedidoActionPerformed(evt);
            }
        });
        pnlBotonesAccion.add(btnFinalizarPedido);

        pnlInferior.add(pnlBotonesAccion, java.awt.BorderLayout.EAST);

        getContentPane().add(pnlInferior, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(cboCategoria.getSelectedIndex());
    }

    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            agregarProductoSeleccionado();
        }
    }

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        agregarProductoSeleccionado();
    }

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {
        quitarProductoSeleccionado();
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarFormulario();
    }

    private void btnVerComprobanteActionPerformed(java.awt.event.ActionEvent evt) {
        verComprobante();
    }

    private void btnFinalizarPedidoActionPerformed(java.awt.event.ActionEvent evt) {
        finalizarPedido();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FrmPedido().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnFinalizarPedido;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnVerComprobante;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JComboBox<String> cboEmpleado;
    private javax.swing.JComboBox<String> cboMetodoPago;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblMetodoPago;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTexto;
    private javax.swing.JPanel pnlAccionesCarrito;
    private javax.swing.JPanel pnlCarrito;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlInferior;
    private javax.swing.JPanel pnlPago;
    private javax.swing.JPanel pnlProductos;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlBotonesAccion;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
