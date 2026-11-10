package vista;

import controlador.GestorCafeteria;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
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
 * FORMULARIO DE PEDIDOS: PanelPedido
 * Cuenta con archivo .form para habilitar la pestaÃ±a [Design] en NetBeans.
 * Permite mover con el mouse los botones, tablas y campos del punto de venta.
 */
public class PanelPedido extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final Color COLOR_FONDO = new Color(200, 155, 90);
    public static final Color COLOR_MARRON_OSCURO = new Color(76, 47, 29);
    public static final Color COLOR_BOTON_HOVER = new Color(126, 77, 47);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;

    // Modelos de tabla
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloDetallePedido;

    // Estado del carrito actual en memoria
    private final transient List<ItemCarritoVisual> itemsCarrito = new ArrayList<>();
    private double totalCalculado = 0.0;

    public PanelPedido() {
        this(new GestorCafeteria());
    }

    public PanelPedido(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        personalizarComponentes();
        cargarEmpleados();
        cargarProductos(0);
    }

    private void personalizarComponentes() {
        // DocumentFilter para 8 dÃ­gitos numÃ©ricos de DNI
        ((AbstractDocument) txtDocumentoCliente.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder();
                for (char c : string.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
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
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                int allowed = 8 - (current.length() - length);
                if (allowed > 0) {
                    String toReplace = sb.length() > allowed ? sb.substring(0, allowed) : sb.toString();
                    super.replace(fb, offset, length, toReplace, attrs);
                }
            }
        });

        // Configurar modelos no editables
        String[] colsProd = {"ID", "Producto", "Precio (S/)", "Stock Disp."};
        modeloProductos = new DefaultTableModel(colsProd, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvProductos.setModel(modeloProductos);
        estilizarTabla(dgvProductos);

        String[] colsDet = {"ID", "Producto", "Precio", "Cant.", "Subtotal (S/)"};
        modeloDetallePedido = new DefaultTableModel(colsDet, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvDetallePedido.setModel(modeloDetallePedido);
        estilizarTabla(dgvDetallePedido);
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.setGridColor(new Color(220, 200, 175));
        tabla.setDefaultEditor(Object.class, null);
        tabla.setCellSelectionEnabled(false);
        tabla.setRowSelectionAllowed(true);

        javax.swing.table.JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setPreferredSize(new Dimension(100, 28));
            header.setReorderingAllowed(false);
            DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    lbl.setBackground(COLOR_MARRON_OSCURO);
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setOpaque(true);
                    lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(55, 32, 18)));
                    return lbl;
                }
            };
            header.setDefaultRenderer(headerRenderer);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            }
        }

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(COLOR_BOTON_HOVER);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 246, 240));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlDatosSuperiores = new javax.swing.JPanel();
        lblNombreCliente = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        lblApellidoCliente = new javax.swing.JLabel();
        txtApellidoCliente = new javax.swing.JTextField();
        lblDocumentoCliente = new javax.swing.JLabel();
        txtDocumentoCliente = new javax.swing.JTextField();
        lblComprobante = new javax.swing.JLabel();
        txtComprobante = new javax.swing.JTextField();
        lblMetodoPago = new javax.swing.JLabel();
        cboMetodoPago = new javax.swing.JComboBox<>();
        lblEmpleado = new javax.swing.JLabel();
        cboEmpleado = new javax.swing.JComboBox<>();
        pnlCentro = new javax.swing.JPanel();
        pnlCategorias = new javax.swing.JPanel();
        btnBebidasCalientes = new javax.swing.JButton();
        btnBebidasFrias = new javax.swing.JButton();
        btnPanaderia = new javax.swing.JButton();
        btnPostres = new javax.swing.JButton();
        btnTodos = new javax.swing.JButton();
        pnlTablas = new javax.swing.JPanel();
        pnlProd = new javax.swing.JPanel();
        lblTituloProd = new javax.swing.JLabel();
        scrollProductos = new javax.swing.JScrollPane();
        dgvProductos = new javax.swing.JTable();
        btnAgregarProd = new javax.swing.JButton();
        pnlDetalle = new javax.swing.JPanel();
        lblTituloDet = new javax.swing.JLabel();
        scrollDetalle = new javax.swing.JScrollPane();
        dgvDetallePedido = new javax.swing.JTable();
        pnlAccionesCarrito = new javax.swing.JPanel();
        btnQuitarProducto = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        pnlInferior = new javax.swing.JPanel();
        pnlTotal = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnGenerarBoleta = new javax.swing.JButton();
        btnFinalizarPedido = new javax.swing.JButton();

        setBackground(new java.awt.Color(200, 155, 90));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setLayout(new java.awt.BorderLayout(10, 10));

        pnlDatosSuperiores.setBackground(new java.awt.Color(200, 155, 90));
        pnlDatosSuperiores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 47, 29), 2), "Datos de AtenciÃ³n y Cliente - Trabajador / Barista", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 13), new java.awt.Color(55, 32, 18))); // NOI18N
        pnlDatosSuperiores.setLayout(new java.awt.GridBagLayout());

        lblNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombreCliente.setForeground(new java.awt.Color(55, 32, 18));
        lblNombreCliente.setText("Nombre Cliente:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblNombreCliente, gridBagConstraints);

        txtNombreCliente.setColumns(14);
        txtNombreCliente.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(txtNombreCliente, gridBagConstraints);

        lblApellidoCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApellidoCliente.setForeground(new java.awt.Color(55, 32, 18));
        lblApellidoCliente.setText("Apellido Cliente:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblApellidoCliente, gridBagConstraints);

        txtApellidoCliente.setColumns(14);
        txtApellidoCliente.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(txtApellidoCliente, gridBagConstraints);

        lblDocumentoCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDocumentoCliente.setForeground(new java.awt.Color(55, 32, 18));
        lblDocumentoCliente.setText("DNI Cliente:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblDocumentoCliente, gridBagConstraints);

        txtDocumentoCliente.setColumns(12);
        txtDocumentoCliente.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(txtDocumentoCliente, gridBagConstraints);

        lblComprobante.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblComprobante.setForeground(new java.awt.Color(55, 32, 18));
        lblComprobante.setText("Comprobante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblComprobante, gridBagConstraints);

        txtComprobante.setEditable(false);
        txtComprobante.setBackground(new java.awt.Color(245, 235, 220));
        txtComprobante.setColumns(14);
        txtComprobante.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComprobante.setForeground(new java.awt.Color(76, 47, 29));
        txtComprobante.setText("Boleta de Venta");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(txtComprobante, gridBagConstraints);

        lblMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMetodoPago.setForeground(new java.awt.Color(55, 32, 18));
        lblMetodoPago.setText("MÃ©todo de Pago:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblMetodoPago, gridBagConstraints);

        cboMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta", "Yape", "Plin" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(cboMetodoPago, gridBagConstraints);

        lblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmpleado.setForeground(new java.awt.Color(55, 32, 18));
        lblEmpleado.setText("Atendido por:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(lblEmpleado, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        pnlDatosSuperiores.add(cboEmpleado, gridBagConstraints);

        add(pnlDatosSuperiores, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(200, 155, 90));
        pnlCentro.setLayout(new java.awt.BorderLayout(8, 8));

        pnlCategorias.setBackground(new java.awt.Color(200, 155, 90));
        pnlCategorias.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));

        btnBebidasCalientes.setBackground(new java.awt.Color(76, 47, 29));
        btnBebidasCalientes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBebidasCalientes.setForeground(new java.awt.Color(255, 255, 255));
        btnBebidasCalientes.setText("Bebidas Calientes");
        btnBebidasCalientes.setFocusable(false);
        btnBebidasCalientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBebidasCalientesActionPerformed(evt);
            }
        });
        pnlCategorias.add(btnBebidasCalientes);

        btnBebidasFrias.setBackground(new java.awt.Color(76, 47, 29));
        btnBebidasFrias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBebidasFrias.setForeground(new java.awt.Color(255, 255, 255));
        btnBebidasFrias.setText("Bebidas FrÃ­as");
        btnBebidasFrias.setFocusable(false);
        btnBebidasFrias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBebidasFriasActionPerformed(evt);
            }
        });
        pnlCategorias.add(btnBebidasFrias);

        btnPanaderia.setBackground(new java.awt.Color(76, 47, 29));
        btnPanaderia.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPanaderia.setForeground(new java.awt.Color(255, 255, 255));
        btnPanaderia.setText("PanaderÃ­a");
        btnPanaderia.setFocusable(false);
        btnPanaderia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanaderiaActionPerformed(evt);
            }
        });
        pnlCategorias.add(btnPanaderia);

        btnPostres.setBackground(new java.awt.Color(76, 47, 29));
        btnPostres.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPostres.setForeground(new java.awt.Color(255, 255, 255));
        btnPostres.setText("Postres");
        btnPostres.setFocusable(false);
        btnPostres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostresActionPerformed(evt);
            }
        });
        pnlCategorias.add(btnPostres);

        btnTodos.setBackground(new java.awt.Color(76, 47, 29));
        btnTodos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTodos.setForeground(new java.awt.Color(255, 255, 255));
        btnTodos.setText("Todos los productos");
        btnTodos.setFocusable(false);
        btnTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodosActionPerformed(evt);
            }
        });
        pnlCategorias.add(btnTodos);

        pnlCentro.add(pnlCategorias, java.awt.BorderLayout.NORTH);

        pnlTablas.setBackground(new java.awt.Color(200, 155, 90));
        pnlTablas.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        pnlProd.setBackground(new java.awt.Color(200, 155, 90));
        pnlProd.setLayout(new java.awt.BorderLayout(4, 4));

        lblTituloProd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTituloProd.setForeground(new java.awt.Color(55, 32, 18));
        lblTituloProd.setText("CatÃ¡logo de Productos (Doble clic para agregar)");
        pnlProd.add(lblTituloProd, java.awt.BorderLayout.NORTH);

        dgvProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Precio (S/)", "Stock Disp."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dgvProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dgvProductosMouseClicked(evt);
            }
        });
        scrollProductos.setViewportView(dgvProductos);

        pnlProd.add(scrollProductos, java.awt.BorderLayout.CENTER);

        btnAgregarProd.setBackground(new java.awt.Color(76, 47, 29));
        btnAgregarProd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarProd.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProd.setText("Agregar Producto al Pedido");
        btnAgregarProd.setFocusable(false);
        btnAgregarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProdActionPerformed(evt);
            }
        });
        pnlProd.add(btnAgregarProd, java.awt.BorderLayout.SOUTH);

        pnlTablas.add(pnlProd);

        pnlDetalle.setBackground(new java.awt.Color(200, 155, 90));
        pnlDetalle.setLayout(new java.awt.BorderLayout(4, 4));

        lblTituloDet.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTituloDet.setForeground(new java.awt.Color(55, 32, 18));
        lblTituloDet.setText("Detalle del Pedido Actual (Carrito)");
        pnlDetalle.add(lblTituloDet, java.awt.BorderLayout.NORTH);

        dgvDetallePedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Precio", "Cant.", "Subtotal (S/)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollDetalle.setViewportView(dgvDetallePedido);

        pnlDetalle.add(scrollDetalle, java.awt.BorderLayout.CENTER);

        pnlAccionesCarrito.setBackground(new java.awt.Color(200, 155, 90));
        pnlAccionesCarrito.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 4));

        btnQuitarProducto.setBackground(new java.awt.Color(76, 47, 29));
        btnQuitarProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnQuitarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitarProducto.setText("Quitar producto");
        btnQuitarProducto.setFocusable(false);
        btnQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductoActionPerformed(evt);
            }
        });
        pnlAccionesCarrito.add(btnQuitarProducto);

        btnLimpiar.setBackground(new java.awt.Color(76, 47, 29));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setFocusable(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        pnlAccionesCarrito.add(btnLimpiar);

        pnlDetalle.add(pnlAccionesCarrito, java.awt.BorderLayout.SOUTH);

        pnlTablas.add(pnlDetalle);

        pnlCentro.add(pnlTablas, java.awt.BorderLayout.CENTER);

        add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlInferior.setBackground(new java.awt.Color(200, 155, 90));
        pnlInferior.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(76, 47, 29)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 8)));
        pnlInferior.setLayout(new java.awt.BorderLayout(10, 10));

        pnlTotal.setBackground(new java.awt.Color(200, 155, 90));
        pnlTotal.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(55, 32, 18));
        lblTotal.setText("Total: S/ 0.00");
        pnlTotal.add(lblTotal);

        pnlInferior.add(pnlTotal, java.awt.BorderLayout.WEST);

        pnlBotones.setBackground(new java.awt.Color(200, 155, 90));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 5));

        btnGenerarBoleta.setBackground(new java.awt.Color(76, 47, 29));
        btnGenerarBoleta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGenerarBoleta.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarBoleta.setText("Ver Comprobante");
        btnGenerarBoleta.setFocusable(false);
        btnGenerarBoleta.setPreferredSize(new java.awt.Dimension(160, 46));
        btnGenerarBoleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarBoletaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnGenerarBoleta);

        btnFinalizarPedido.setBackground(new java.awt.Color(76, 47, 29));
        btnFinalizarPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnFinalizarPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalizarPedido.setText("Finalizar Pedido");
        btnFinalizarPedido.setFocusable(false);
        btnFinalizarPedido.setPreferredSize(new java.awt.Dimension(160, 46));
        btnFinalizarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarPedidoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnFinalizarPedido);

        pnlInferior.add(pnlBotones, java.awt.BorderLayout.EAST);

        add(pnlInferior, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBebidasCalientesActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(1);
    }

    private void btnBebidasFriasActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(2);
    }

    private void btnPanaderiaActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(3);
    }

    private void btnPostresActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(4);
    }

    private void btnTodosActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductos(0);
    }

    private void btnAgregarProdActionPerformed(java.awt.event.ActionEvent evt) {
        agregarProductoSeleccionado();
    }

    private void btnQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        quitarProductoSeleccionado();
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarFormulario();
    }

    private void btnGenerarBoletaActionPerformed(java.awt.event.ActionEvent evt) {
        previsualizarComprobante();
    }

    private void btnFinalizarPedidoActionPerformed(java.awt.event.ActionEvent evt) {
        finalizarPedido();
    }

    private void dgvProductosMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            agregarProductoSeleccionado();
        }
    }

    public void cargarProductos(int idCategoria) {
        modeloProductos.setRowCount(0);
        List<Producto> lista = (idCategoria == 0)
                ? gestor.getProductos()
                : gestor.filtrarProductos(idCategoria, "");

        for (Producto p : lista) {
            int stockVisual = p.getStock() - obtenerCantidadEnCarrito(p.getIdProducto());
            modeloProductos.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                String.format("%.2f", p.getPrecio()),
                Math.max(stockVisual, 0)
            });
        }
    }

    private void cargarEmpleados() {
        cboEmpleado.removeAllItems();
        for (Empleado e : gestor.getEmpleados()) {
            cboEmpleado.addItem(e.getNombres() + " " + e.getApellidos() + " - " + e.getCargo());
        }
    }

    private int obtenerCantidadEnCarrito(int idProducto) {
        for (ItemCarritoVisual item : itemsCarrito) {
            if (item.producto.getIdProducto() == idProducto) {
                return item.cantidad;
            }
        }
        return 0;
    }

    private void agregarProductoSeleccionado() {
        int fila = dgvProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un producto de la tabla para agregarlo al pedido.",
                "Seleccionar producto", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idProducto = (int) modeloProductos.getValueAt(fila, 0);
        Producto prod = gestor.buscarProductoPorId(idProducto);
        if (prod == null) return;

        int stockVisual = (int) modeloProductos.getValueAt(fila, 3);
        if (stockVisual <= 0) {
            JOptionPane.showMessageDialog(this,
                "El producto seleccionado no tiene stock disponible.",
                "Stock no disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemCarritoVisual existente = null;
        for (ItemCarritoVisual it : itemsCarrito) {
            if (it.producto.getIdProducto() == idProducto) {
                existente = it;
                break;
            }
        }

        if (existente == null) {
            itemsCarrito.add(new ItemCarritoVisual(prod, 1));
        } else {
            existente.cantidad += 1;
        }

        actualizarTablaCarrito();
        cargarProductos(0);
    }

    private void quitarProductoSeleccionado() {
        int fila = dgvDetallePedido.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un producto del detalle para quitarlo.",
                "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (int) modeloDetallePedido.getValueAt(fila, 0);
        itemsCarrito.removeIf(it -> it.producto.getIdProducto() == idProducto);

        actualizarTablaCarrito();
        cargarProductos(0);
    }

    private void actualizarTablaCarrito() {
        modeloDetallePedido.setRowCount(0);
        double subtotalBruto = 0.0;

        for (ItemCarritoVisual it : itemsCarrito) {
            double subt = it.cantidad * it.producto.getPrecio();
            subtotalBruto += subt;
            modeloDetallePedido.addRow(new Object[]{
                it.producto.getIdProducto(),
                it.producto.getNombreProducto(),
                String.format("%.2f", it.producto.getPrecio()),
                it.cantidad,
                String.format("%.2f", subt)
            });
        }

        totalCalculado = Math.round(subtotalBruto * 100.0) / 100.0;
        lblTotal.setText(String.format("Total: S/ %.2f", totalCalculado));
    }

    public void limpiarFormulario() {
        txtNombreCliente.setText("");
        txtApellidoCliente.setText("");
        txtDocumentoCliente.setText("");
        cboMetodoPago.setSelectedIndex(0);
        if (cboEmpleado.getItemCount() > 0) {
            cboEmpleado.setSelectedIndex(0);
        }
        itemsCarrito.clear();
        actualizarTablaCarrito();
        cargarProductos(0);
    }

    private void finalizarPedido() {
        if (itemsCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe agregar al menos un producto al pedido.",
                "Pedido vacÃ­o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doc = txtDocumentoCliente.getText().trim();
        if (doc.length() != 8 || !doc.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this,
                "El DNI del cliente es obligatorio y debe tener exactamente 8 dÃ­gitos numÃ©ricos.",
                "DNI Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtDocumentoCliente.requestFocus();
            return;
        }

        String nombres = txtNombreCliente.getText().trim();
        String apellidos = txtApellidoCliente.getText().trim();
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar el nombre del cliente.",
                "Nombre Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtNombreCliente.requestFocus();
            return;
        }
        if (apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar el apellido del cliente.",
                "Apellido Obligatorio", JOptionPane.WARNING_MESSAGE);
            txtApellidoCliente.requestFocus();
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this,
                "Â¿Desea finalizar este pedido y emitir Boleta de Venta?",
                "Confirmar Pedido", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int idxEmp = cboEmpleado.getSelectedIndex();
            Empleado empleado = (idxEmp >= 0 && idxEmp < gestor.getEmpleados().size())
                    ? gestor.getEmpleados().get(idxEmp)
                    : gestor.getEmpleados().get(0);

            Cliente cliente = new Cliente(gestor.getClientes().size() + 1, nombres, apellidos, doc, "987654321", "cliente@aurora.pe");

            int nuevoId = gestor.getPedidos().size() + 1;
            Pedido pedido = new Pedido(
                    nuevoId,
                    cliente,
                    empleado,
                    (String) cboMetodoPago.getSelectedItem(),
                    "En local"
            );

            for (ItemCarritoVisual it : itemsCarrito) {
                pedido.agregarItem(it.producto, it.cantidad, "Regular", "Caliente", "Ninguno");
            }

            ComprobantePago comprobante = gestor.registrarPedidoYComprobante(pedido, "BOLETA", doc, "", "");

            String msg = String.format(
                "Â¡Boleta N. %s registrada correctamente!\n\n" +
                "Cliente: %s %s\n" +
                "DNI: %s\n" +
                "Atendido por: %s (%s)\n" +
                "Total Pagado: S/ %.2f\n\n" +
                "Archivo generado en: comprobantes/%s.txt",
                comprobante.getNumeroSerie(),
                cliente.getNombres(), cliente.getApellidos(),
                doc,
                empleado.getNombres(), empleado.getCargo(),
                pedido.calcularTotal(),
                comprobante.getTipoComprobante() + "_" + comprobante.getNumeroSerie().replace("-", "_")
            );

            JOptionPane.showMessageDialog(this, msg, "Pedido Guardado - CafeterÃ­a Aurora", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar pedido: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void previsualizarComprobante() {
        if (itemsCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agregue productos para visualizar el comprobante.",
                "Sin productos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int idxEmp = Math.max(cboEmpleado.getSelectedIndex(), 0);
            Empleado emp = gestor.getEmpleados().get(idxEmp);
            String nom = txtNombreCliente.getText().trim();
            String ape = txtApellidoCliente.getText().trim();
            String doc = txtDocumentoCliente.getText().trim();
            if (doc.length() != 8 || !doc.matches("\\d{8}")) {
                JOptionPane.showMessageDialog(this,
                    "Para ver el comprobante, el DNI debe tener exactamente 8 dÃ­gitos.",
                    "DNI Obligatorio", JOptionPane.WARNING_MESSAGE);
                txtDocumentoCliente.requestFocus();
                return;
            }
            if (nom.isEmpty()) nom = "Consumidor";
            if (ape.isEmpty()) ape = "Final";

            Cliente cl = new Cliente(99, nom, ape, doc, "987654321", "cliente@aurora.pe");

            Pedido pTemp = new Pedido(gestor.getPedidos().size() + 1, cl, emp,
                    (String) cboMetodoPago.getSelectedItem(), "En local");

            for (ItemCarritoVisual it : itemsCarrito) {
                pTemp.agregarItem(it.producto, it.cantidad);
            }

            String fecha = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            modelo.Boleta bol = new modelo.Boleta("B001-009999", fecha, pTemp, doc);
            String voucher = bol.generarContenidoImprimible();

            JDialog dlg = new JDialog((java.awt.Frame) null, "Vista Previa de Boleta ElectrÃ³nica", true);
            dlg.setSize(480, 560);
            dlg.setLocationRelativeTo(this);
            JTextArea area = new JTextArea(voucher);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setEditable(false);
            dlg.add(new JScrollPane(area), java.awt.BorderLayout.CENTER);
            dlg.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar comprobante: " + ex.getMessage());
        }
    }

    private static class ItemCarritoVisual {
        Producto producto;
        int cantidad;

        ItemCarritoVisual(Producto p, int c) {
            this.producto = p;
            this.cantidad = c;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProd;
    private javax.swing.JButton btnBebidasCalientes;
    private javax.swing.JButton btnBebidasFrias;
    private javax.swing.JButton btnFinalizarPedido;
    private javax.swing.JButton btnGenerarBoleta;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnPanaderia;
    private javax.swing.JButton btnPostres;
    private javax.swing.JButton btnQuitarProducto;
    private javax.swing.JButton btnTodos;
    private javax.swing.JComboBox<String> cboEmpleado;
    private javax.swing.JComboBox<String> cboMetodoPago;
    private javax.swing.JTable dgvDetallePedido;
    private javax.swing.JTable dgvProductos;
    private javax.swing.JLabel lblApellidoCliente;
    private javax.swing.JLabel lblComprobante;
    private javax.swing.JLabel lblDocumentoCliente;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblMetodoPago;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblTituloDet;
    private javax.swing.JLabel lblTituloProd;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlAccionesCarrito;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCategorias;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlDatosSuperiores;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlInferior;
    private javax.swing.JPanel pnlProd;
    private javax.swing.JPanel pnlTablas;
    private javax.swing.JPanel pnlTotal;
    private javax.swing.JScrollPane scrollDetalle;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JTextField txtApellidoCliente;
    private javax.swing.JTextField txtComprobante;
    private javax.swing.JTextField txtDocumentoCliente;
    private javax.swing.JTextField txtNombreCliente;
    // End of variables declaration//GEN-END:variables
}
