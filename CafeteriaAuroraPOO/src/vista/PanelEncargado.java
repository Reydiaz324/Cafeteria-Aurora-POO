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
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;

public class PanelEncargado extends JPanel {
    private static final long serialVersionUID = 1L;

    // Paleta exacta de C#
    public static final Color COLOR_FONDO = new Color(200, 155, 90);
    public static final Color COLOR_MARRON_OSCURO = new Color(76, 47, 29);
    public static final Color COLOR_BOTON_HOVER = new Color(126, 77, 47);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;

    // Resumen General y Selector de Período
    private JComboBox<String> cboFiltroMes;
    private JTextField txtTotalVendido;
    private JTextField txtCantidadPedidos;
    private JTextField txtTicketPromedio;

    // Gestión de Stock
    private JComboBox<String> cmbProductos;
    private JTextField txtCantidadAgregar;

    // Reportes
    private JLabel lblTabla;
    private JTable dgvReportes;
    private DefaultTableModel modeloReportes;
    private int tipoReporteActivo = 1; // 1: Mas vendidos, 2: Stock, 3: Ventas por mes, 4: Metodo pago, 5: Clientes, 6: Empleados

    public PanelEncargado(GestorCafeteria gestor) {
        this.gestor = gestor;
        initUI();
        cargarResumenGeneral();
        cargarComboBoxProductos();
        cargarProductosMasVendidos();
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(16, 20, 16, 20));

        // 1. Título y Resumen General Superior
        JPanel pnlNorte = new JPanel(new BorderLayout(8, 8));
        pnlNorte.setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("Panel del encargado");
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        pnlNorte.add(lblTitulo, BorderLayout.NORTH);

        pnlNorte.add(crearPanelResumen(), BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        // 2. Centro: Controles de Gestión de Stock + Botones de Reporte + Tabla de Reportes
        JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
        pnlCentro.setBackground(COLOR_FONDO);

        JPanel pnlControlesSuperiores = new JPanel(new BorderLayout(8, 8));
        pnlControlesSuperiores.setBackground(COLOR_FONDO);
        pnlControlesSuperiores.add(crearPanelStock(), BorderLayout.NORTH);
        pnlControlesSuperiores.add(crearPanelBotonesReportes(), BorderLayout.SOUTH);
        pnlCentro.add(pnlControlesSuperiores, BorderLayout.NORTH);

        JPanel pnlTablaContainer = new JPanel(new BorderLayout(6, 6));
        pnlTablaContainer.setBackground(COLOR_FONDO);

        lblTabla = new JLabel("Productos más vendidos");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTabla.setForeground(COLOR_TEXTO_TITULO);
        pnlTablaContainer.add(lblTabla, BorderLayout.NORTH);

        modeloReportes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dgvReportes = new JTable(modeloReportes) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dgvReportes.setDefaultEditor(Object.class, null);
        estilizarTabla(dgvReportes);
        pnlTablaContainer.add(new JScrollPane(dgvReportes), BorderLayout.CENTER);

        pnlCentro.add(pnlTablaContainer, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);
    }

    private JPanel crearPanelResumen() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(COLOR_MARRON_OSCURO);
        pnl.setBorder(new EmptyBorder(10, 16, 10, 16));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 10, 4, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Título y Selector de Mes
        JLabel lblResumen = new JLabel("RESUMEN DE VENTAS");
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResumen.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        pnl.add(lblResumen, g);

        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pnlFiltro.setOpaque(false);
        JLabel lblFil = new JLabel("Filtrar por Mes:");
        lblFil.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFil.setForeground(Color.WHITE);
        pnlFiltro.add(lblFil);

        cboFiltroMes = new JComboBox<>(new String[]{
            "Octubre 2026 (Mes Actual)",
            "Septiembre 2026",
            "Agosto 2026",
            "Julio 2026",
            "Junio 2026",
            "Mayo 2026",
            "Abril 2026",
            "Marzo 2026",
            "Febrero 2026",
            "Enero 2026",
            "Todo el Año 2026 (Acumulado)"
        });
        cboFiltroMes.setPreferredSize(new Dimension(215, 26));
        cboFiltroMes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboFiltroMes.addActionListener(e -> actualizarResumenYReporte());
        pnlFiltro.add(cboFiltroMes);

        g.gridx = 2; g.gridy = 0; g.gridwidth = 1;
        pnl.add(pnlFiltro, g);

        // Fila 1 y 2: Tarjetas de Resumen
        g.gridwidth = 1;

        g.gridx = 0; g.gridy = 1;
        JLabel l1 = new JLabel("Total vendido:");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l1.setForeground(Color.WHITE);
        pnl.add(l1, g);

        g.gridx = 0; g.gridy = 2;
        txtTotalVendido = new JTextField(10);
        configurarCampoResumen(txtTotalVendido);
        pnl.add(txtTotalVendido, g);

        g.gridx = 1; g.gridy = 1;
        JLabel l2 = new JLabel("Cantidad pedidos:");
        l2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l2.setForeground(Color.WHITE);
        pnl.add(l2, g);

        g.gridx = 1; g.gridy = 2;
        txtCantidadPedidos = new JTextField(10);
        configurarCampoResumen(txtCantidadPedidos);
        pnl.add(txtCantidadPedidos, g);

        g.gridx = 2; g.gridy = 1;
        JLabel l3 = new JLabel("Ticket promedio:");
        l3.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l3.setForeground(Color.WHITE);
        pnl.add(l3, g);

        g.gridx = 2; g.gridy = 2;
        txtTicketPromedio = new JTextField(10);
        configurarCampoResumen(txtTicketPromedio);
        pnl.add(txtTicketPromedio, g);

        return pnl;
    }

    private void configurarCampoResumen(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txt.setEditable(false);
        txt.setBackground(Color.WHITE);
        txt.setForeground(COLOR_MARRON_OSCURO);
        txt.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private JPanel crearPanelStock() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        pnl.setBackground(COLOR_FONDO);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_MARRON_OSCURO, 1),
                "Actualizar Stock de Producto",
                0, 0, new Font("Segoe UI", Font.BOLD, 11), COLOR_TEXTO_TITULO));

        JLabel lblProd = new JLabel("Producto:");
        lblProd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblProd.setForeground(COLOR_TEXTO_TITULO);
        pnl.add(lblProd);

        cmbProductos = new JComboBox<>();
        cmbProductos.setPreferredSize(new Dimension(240, 26));
        pnl.add(cmbProductos);

        JLabel lblCant = new JLabel("Cantidad a agregar:");
        lblCant.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblCant.setForeground(COLOR_TEXTO_TITULO);
        pnl.add(lblCant);

        txtCantidadAgregar = new JTextField(6);
        pnl.add(txtCantidadAgregar);

        BotonFlat btnAgregarStock = new BotonFlat("Agregar stock", BotonFlat.Estilo.ACCION_CAFE);
        btnAgregarStock.addActionListener(e -> agregarStock());
        pnl.add(btnAgregarStock);

        return pnl;
    }

    private JPanel crearPanelBotonesReportes() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnl.setBackground(COLOR_FONDO);

        BotonFlat btn1 = new BotonFlat("Productos más vendidos", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btn2 = new BotonFlat("Stock actual", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btn3 = new BotonFlat("Ventas por mes", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btn4 = new BotonFlat("Ventas por método de pago", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btn5 = new BotonFlat("Clientes frecuentes", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btn6 = new BotonFlat("Pedidos por empleado", BotonFlat.Estilo.ACCION_CAFE);
        BotonFlat btnExp = new BotonFlat("Exportar Reporte TXT", BotonFlat.Estilo.ACCION_CAFE);

        btn1.addActionListener(e -> cargarProductosMasVendidos());
        btn2.addActionListener(e -> cargarStockActual());
        btn3.addActionListener(e -> cargarVentasPorMes());
        btn4.addActionListener(e -> cargarVentasMetodoPago());
        btn5.addActionListener(e -> cargarClientesFrecuentes());
        btn6.addActionListener(e -> cargarPedidosPorEmpleado());
        btnExp.addActionListener(e -> exportarReporteEjecutivo());

        pnl.add(btn1);
        pnl.add(btn2);
        pnl.add(btn3);
        pnl.add(btn4);
        pnl.add(btn5);
        pnl.add(btn6);
        pnl.add(btnExp);

        return pnl;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.setGridColor(new Color(220, 200, 175));
        tabla.setDefaultEditor(Object.class, null);
        tabla.setCellSelectionEnabled(false);
        tabla.setRowSelectionAllowed(true);

        aplicarEstiloHeader(tabla);

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

    private void aplicarEstiloHeader(JTable tabla) {
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
    }

    private int obtenerMesSeleccionado() {
        if (cboFiltroMes == null) return 10;
        int idx = cboFiltroMes.getSelectedIndex();
        if (idx == 10) return 0; // Todo el año
        return 10 - idx;
    }

    private String obtenerNombreMesSeleccionado() {
        int m = obtenerMesSeleccionado();
        switch (m) {
            case 10: return "Octubre 2026";
            case 9: return "Septiembre 2026";
            case 8: return "Agosto 2026";
            case 7: return "Julio 2026";
            case 6: return "Junio 2026";
            case 5: return "Mayo 2026";
            case 4: return "Abril 2026";
            case 3: return "Marzo 2026";
            case 2: return "Febrero 2026";
            case 1: return "Enero 2026";
            default: return "Todo el Año 2026";
        }
    }

    public void actualizarResumenYReporte() {
        cargarResumenGeneral();
        switch (tipoReporteActivo) {
            case 1: cargarProductosMasVendidos(); break;
            case 2: cargarStockActual(); break;
            case 3: cargarVentasPorMes(); break;
            case 4: cargarVentasMetodoPago(); break;
            case 5: cargarClientesFrecuentes(); break;
            case 6: cargarPedidosPorEmpleado(); break;
        }
    }

    // Lógica de datos del Encargado
    public void cargarResumenGeneral() {
        int mes = obtenerMesSeleccionado();
        double[] resumen = gestor.obtenerResumenGeneralVentas(mes, 2026);
        txtTotalVendido.setText(String.format("S/ %.2f", resumen[0]));
        txtCantidadPedidos.setText(String.valueOf((int) resumen[1]));
        txtTicketPromedio.setText(String.format("S/ %.2f", resumen[2]));
    }

    public void cargarComboBoxProductos() {
        cmbProductos.removeAllItems();
        for (Producto p : gestor.getProductos()) {
            cmbProductos.addItem(p.getIdProducto() + " - " + p.getNombreProducto() + " (Stock: " + p.getStock() + ")");
        }
    }

    private void agregarStock() {
        int idx = cmbProductos.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantStr = txtCantidadAgregar.getText().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCantidadAgregar.requestFocus();
            return;
        }

        Producto p = gestor.getProductos().get(idx);
        try {
            gestor.agregarStockProducto(p.getIdProducto(), cantidad);
            JOptionPane.showMessageDialog(this,
                "Stock actualizado correctamente para: " + p.getNombreProducto() +
                "\nNuevo stock disponible: " + p.getStock(),
                "Stock Actualizado", JOptionPane.INFORMATION_MESSAGE);

            txtCantidadAgregar.setText("");
            cargarComboBoxProductos();
            cargarResumenGeneral();
            cargarStockActual();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar stock: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarProductosMasVendidos() {
        tipoReporteActivo = 1;
        lblTabla.setText("Productos más vendidos - " + obtenerNombreMesSeleccionado());
        String[] cols = {"Producto", "Cantidad Vendida", "Total Recaudado (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteProductosMasVendidos(obtenerMesSeleccionado(), 2026);
        for (Object[] fila : datos) {
            modeloReportes.addRow(new Object[]{
                fila[0],
                fila[1],
                String.format("S/ %.2f", (Double) fila[2])
            });
        }
    }

    public void cargarStockActual() {
        tipoReporteActivo = 2;
        lblTabla.setText("Stock actual de productos");
        String[] cols = {"Producto", "Categoría", "Stock Actual", "Proveedor", "Estado"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteStockActual();
        for (Object[] fila : datos) {
            modeloReportes.addRow(fila);
        }
    }

    public void cargarVentasPorMes() {
        tipoReporteActivo = 3;
        lblTabla.setText("Ventas agrupadas por mes (Histórico 2026)");
        String[] cols = {"Mes / Período", "Cantidad de Pedidos", "Total Vendido (S/)", "Ticket Promedio (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteVentasPorMes();
        for (Object[] fila : datos) {
            modeloReportes.addRow(new Object[]{
                fila[0],
                fila[1],
                String.format("S/ %.2f", (Double) fila[2]),
                String.format("S/ %.2f", (Double) fila[3])
            });
        }
    }

    public void cargarVentasMetodoPago() {
        tipoReporteActivo = 4;
        lblTabla.setText("Ventas por método de pago - " + obtenerNombreMesSeleccionado());
        String[] cols = {"Método de Pago", "Cantidad de Pedidos", "Total Vendido (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteVentasMetodoPago(obtenerMesSeleccionado(), 2026);
        for (Object[] fila : datos) {
            modeloReportes.addRow(new Object[]{
                fila[0],
                fila[1],
                String.format("S/ %.2f", (Double) fila[2])
            });
        }
    }

    public void cargarClientesFrecuentes() {
        tipoReporteActivo = 5;
        lblTabla.setText("Clientes frecuentes - " + obtenerNombreMesSeleccionado());
        String[] cols = {"Cliente", "Pedidos Realizados", "Total Consumido (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteClientesFrecuentes(obtenerMesSeleccionado(), 2026);
        for (Object[] fila : datos) {
            modeloReportes.addRow(new Object[]{
                fila[0],
                fila[1],
                String.format("S/ %.2f", (Double) fila[2])
            });
        }
    }

    public void cargarPedidosPorEmpleado() {
        tipoReporteActivo = 6;
        lblTabla.setText("Pedidos atendidos por empleado - " + obtenerNombreMesSeleccionado());
        String[] cols = {"Empleado", "Cargo", "Pedidos Atendidos", "Total Atendido (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteEmpleadosMasPedidos(obtenerMesSeleccionado(), 2026);
        for (Object[] fila : datos) {
            modeloReportes.addRow(new Object[]{
                fila[0],
                fila[1],
                fila[2],
                String.format("S/ %.2f", (Double) fila[3])
            });
        }
    }

    private void exportarReporteEjecutivo() {
        try {
            String texto = gestor.generarTextoReporteGeneral();
            File carpeta = new File("reportes");
            if (!carpeta.exists()) carpeta.mkdirs();

            File archivo = new File(carpeta, "Reporte_Ejecutivo_Aurora.txt");
            try (FileWriter fw = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                fw.write(texto);
            }

            JOptionPane.showMessageDialog(this,
                "Reporte ejecutivo exportado con éxito en:\n" + archivo.getAbsolutePath(),
                "Reporte Exportado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
