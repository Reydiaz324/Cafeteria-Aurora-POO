package vista;

import controlador.GestorCafeteria;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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

public class PanelPedido extends JPanel {
    private static final long serialVersionUID = 1L;

    // Paleta exacta de C#
    public static final Color COLOR_FONDO = new Color(200, 155, 90);
    public static final Color COLOR_MARRON_OSCURO = new Color(76, 47, 29);
    public static final Color COLOR_BOTON_HOVER = new Color(126, 77, 47);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;

    // Controles de Cliente y Pedido
    private JTextField txtNombreCliente;
    private JTextField txtApellidoCliente;
    private JTextField txtDocumentoCliente;
    private JComboBox<String> cboMetodoPago;
    private JComboBox<String> cboEmpleado;

    // Tablas
    private JTable dgvProductos;
    private DefaultTableModel modeloProductos;
    private JTable dgvDetallePedido;
    private DefaultTableModel modeloDetallePedido;

    // Botones de acción usando BotonFlat
    private BotonFlat btnQuitarProducto;
    private BotonFlat btnLimpiar;
    private BotonFlat btnFinalizarPedido;
    private BotonFlat btnGenerarBoleta;
    private JLabel lblTotal;

    // Estado del carrito actual en memoria
    private final transient List<ItemCarritoVisual> itemsCarrito = new ArrayList<>();
    private double totalCalculado = 0.0;

    public PanelPedido(GestorCafeteria gestor) {
        this.gestor = gestor;
        initUI();
        cargarEmpleados();
        cargarProductos(0); // 0 = todos
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(12, 16, 12, 16));

        // 1. Panel Superior: Datos del Cliente, Barista y Atención
        add(crearPanelDatosSuperiores(), BorderLayout.NORTH);

        // 2. Panel Central: Categorías, Catálogo de Productos y Carrito
        add(crearPanelCentro(), BorderLayout.CENTER);

        // 3. Panel Inferior: Totales y Botones de Finalización
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelDatosSuperiores() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(COLOR_FONDO);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_MARRON_OSCURO, 2),
                "Datos de Atención y Cliente - Trabajador / Barista",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), COLOR_TEXTO_TITULO));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0
        g.gridx = 0; g.gridy = 0;
        pnl.add(crearLabel("Nombre Cliente:"), g);
        g.gridx = 1;
        txtNombreCliente = new JTextField("", 14);
        pnl.add(txtNombreCliente, g);

        g.gridx = 2;
        pnl.add(crearLabel("Apellido Cliente:"), g);
        g.gridx = 3;
        txtApellidoCliente = new JTextField("", 14);
        pnl.add(txtApellidoCliente, g);

        g.gridx = 4;
        pnl.add(crearLabel("DNI Cliente:"), g);
        g.gridx = 5;
        txtDocumentoCliente = new JTextField("", 12);
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
        pnl.add(txtDocumentoCliente, g);

        // Fila 1
        g.gridx = 0; g.gridy = 1;
        pnl.add(crearLabel("Comprobante:"), g);
        g.gridx = 1;
        JTextField txtComprobante = new JTextField("Boleta de Venta", 14);
        txtComprobante.setEditable(false);
        txtComprobante.setBackground(new Color(245, 235, 220));
        txtComprobante.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtComprobante.setForeground(COLOR_MARRON_OSCURO);
        pnl.add(txtComprobante, g);

        g.gridx = 2;
        pnl.add(crearLabel("Método de Pago:"), g);
        g.gridx = 3;
        cboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Yape", "Plin"});
        pnl.add(cboMetodoPago, g);

        g.gridx = 4;
        pnl.add(crearLabel("Atendido por:"), g);
        g.gridx = 5;
        cboEmpleado = new JComboBox<>();
        pnl.add(cboEmpleado, g);

        return pnl;
    }

    private JPanel crearPanelCentro() {
        JPanel pnl = new JPanel(new BorderLayout(8, 8));
        pnl.setBackground(COLOR_FONDO);

        // Barra de botones de Categoría (Idéntico a C# con BotonFlat)
        JPanel pnlCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlCategorias.setBackground(COLOR_FONDO);

        BotonFlat btnBebidasCalientes = new BotonFlat("Bebidas Calientes", BotonFlat.Estilo.CATEGORIA);
        BotonFlat btnBebidasFrias = new BotonFlat("Bebidas Frías", BotonFlat.Estilo.CATEGORIA);
        BotonFlat btnPanaderia = new BotonFlat("Panadería", BotonFlat.Estilo.CATEGORIA);
        BotonFlat btnPostres = new BotonFlat("Postres", BotonFlat.Estilo.CATEGORIA);
        BotonFlat btnTodos = new BotonFlat("Todos los productos", BotonFlat.Estilo.CATEGORIA);

        btnBebidasCalientes.addActionListener(e -> cargarProductos(1));
        btnBebidasFrias.addActionListener(e -> cargarProductos(2));
        btnPanaderia.addActionListener(e -> cargarProductos(3));
        btnPostres.addActionListener(e -> cargarProductos(4));
        btnTodos.addActionListener(e -> cargarProductos(0));

        pnlCategorias.add(btnBebidasCalientes);
        pnlCategorias.add(btnBebidasFrias);
        pnlCategorias.add(btnPanaderia);
        pnlCategorias.add(btnPostres);
        pnlCategorias.add(btnTodos);

        pnl.add(pnlCategorias, BorderLayout.NORTH);

        // Grid con 2 tablas: Izquierda Catálogo Productos (dgvProductos), Derecha Detalle Pedido (dgvDetallePedido)
        JPanel pnlTablas = new JPanel(new GridLayout(1, 2, 12, 0));
        pnlTablas.setBackground(COLOR_FONDO);

        // Tabla Productos
        JPanel pnlProd = new JPanel(new BorderLayout(4, 4));
        pnlProd.setBackground(COLOR_FONDO);
        JLabel lblTituloProd = new JLabel("Catálogo de Productos (Doble clic para agregar)");
        lblTituloProd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTituloProd.setForeground(COLOR_TEXTO_TITULO);
        pnlProd.add(lblTituloProd, BorderLayout.NORTH);

        String[] colsProd = {"ID", "Producto", "Precio (S/)", "Stock Disp."};
        modeloProductos = new DefaultTableModel(colsProd, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvProductos = new JTable(modeloProductos) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvProductos.setDefaultEditor(Object.class, null);
        estilizarTabla(dgvProductos);
        dgvProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    agregarProductoSeleccionado();
                }
            }
        });
        pnlProd.add(new JScrollPane(dgvProductos), BorderLayout.CENTER);

        BotonFlat btnAgregarProd = new BotonFlat("Agregar Producto al Pedido", BotonFlat.Estilo.ACCION_CAFE);
        btnAgregarProd.addActionListener(e -> agregarProductoSeleccionado());
        pnlProd.add(btnAgregarProd, BorderLayout.SOUTH);

        pnlTablas.add(pnlProd);

        // Tabla Detalle Pedido (Carrito)
        JPanel pnlDetalle = new JPanel(new BorderLayout(4, 4));
        pnlDetalle.setBackground(COLOR_FONDO);
        JLabel lblTituloDet = new JLabel("Detalle del Pedido Actual (Carrito)");
        lblTituloDet.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTituloDet.setForeground(COLOR_TEXTO_TITULO);
        pnlDetalle.add(lblTituloDet, BorderLayout.NORTH);

        String[] colsDet = {"ID", "Producto", "Precio", "Cant.", "Subtotal (S/)"};
        modeloDetallePedido = new DefaultTableModel(colsDet, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvDetallePedido = new JTable(modeloDetallePedido) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dgvDetallePedido.setDefaultEditor(Object.class, null);
        estilizarTabla(dgvDetallePedido);
        pnlDetalle.add(new JScrollPane(dgvDetallePedido), BorderLayout.CENTER);

        // Botones de acción del carrito
        JPanel pnlAccionesCarrito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnlAccionesCarrito.setBackground(COLOR_FONDO);
        btnQuitarProducto = new BotonFlat("Quitar producto", BotonFlat.Estilo.ACCION_CAFE);
        btnLimpiar = new BotonFlat("Limpiar", BotonFlat.Estilo.ACCION_CAFE);

        btnQuitarProducto.addActionListener(e -> quitarProductoSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlAccionesCarrito.add(btnQuitarProducto);
        pnlAccionesCarrito.add(btnLimpiar);
        pnlDetalle.add(pnlAccionesCarrito, BorderLayout.SOUTH);

        pnlTablas.add(pnlDetalle);
        pnl.add(pnlTablas, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel crearPanelInferior() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(COLOR_FONDO);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_MARRON_OSCURO),
                new EmptyBorder(8, 0, 0, 0)));

        // Izquierda: Resumen visual de Total
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlTotal.setBackground(COLOR_FONDO);
        lblTotal = new JLabel("Total: S/ 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(COLOR_TEXTO_TITULO);
        pnlTotal.add(lblTotal);
        pnl.add(pnlTotal, BorderLayout.WEST);

        // Derecha: Botones de Cierre (Finalizar Pedido, Boleta)
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        pnlBotones.setBackground(COLOR_FONDO);

        btnFinalizarPedido = new BotonFlat("Finalizar Pedido", BotonFlat.Estilo.ACCION_CAFE);
        btnGenerarBoleta = new BotonFlat("Ver Comprobante", BotonFlat.Estilo.ACCION_CAFE);

        btnFinalizarPedido.setPreferredSize(new Dimension(160, 46));
        btnGenerarBoleta.setPreferredSize(new Dimension(160, 46));

        btnFinalizarPedido.addActionListener(e -> finalizarPedido());
        btnGenerarBoleta.addActionListener(e -> previsualizarComprobante());

        pnlBotones.add(btnGenerarBoleta);
        pnlBotones.add(btnFinalizarPedido);
        pnl.add(pnlBotones, BorderLayout.EAST);

        return pnl;
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXTO_TITULO);
        return lbl;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.setGridColor(new Color(220, 200, 175));
        tabla.setDefaultEditor(Object.class, null);
        tabla.setCellSelectionEnabled(false);
        tabla.setRowSelectionAllowed(true);

        // Encabezado visible con contraste alto: fondo marrón café y letras blancas nítidas
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

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
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

    // Lógica operativa del Trabajador
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
                "Pedido vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doc = txtDocumentoCliente.getText().trim();
        if (doc.length() != 8 || !doc.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this,
                "El DNI del cliente es obligatorio y debe tener exactamente 8 dígitos numéricos.",
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
                "¿Desea finalizar este pedido y emitir Boleta de Venta?",
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
                "¡Boleta N. %s registrada correctamente!\n\n" +
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

            JOptionPane.showMessageDialog(this, msg, "Pedido Guardado - Cafetería Aurora", JOptionPane.INFORMATION_MESSAGE);

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
                    "Para ver el comprobante, el DNI debe tener exactamente 8 dígitos.",
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

            JDialog dlg = new JDialog((java.awt.Frame) null, "Vista Previa de Boleta Electrónica", true);
            dlg.setSize(480, 560);
            dlg.setLocationRelativeTo(this);
            JTextArea area = new JTextArea(voucher);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setEditable(false);
            dlg.add(new JScrollPane(area), BorderLayout.CENTER);
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
}
