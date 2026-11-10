package vista;

import controlador.GestorCafeteria;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;

/**
 * FORMULARIO DEL ENCARGADO: PanelEncargado
 * Cuenta con archivo .form para habilitar la pestaÃ±a [Design] en NetBeans.
 * Permite mover con el mouse los botones de reportes, combos de filtrado y tablas.
 */
public class PanelEncargado extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final Color COLOR_FONDO = new Color(200, 155, 90);
    public static final Color COLOR_MARRON_OSCURO = new Color(76, 47, 29);
    public static final Color COLOR_BOTON_HOVER = new Color(126, 77, 47);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;
    private DefaultTableModel modeloReportes;
    private int tipoReporteActivo = 1;

    public PanelEncargado() {
        this(new GestorCafeteria());
    }

    public PanelEncargado(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        personalizarComponentes();
        cargarResumenGeneral();
        cargarComboBoxProductos();
        cargarProductosMasVendidos();
    }

    private void personalizarComponentes() {
        modeloReportes = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dgvReportes.setModel(modeloReportes);
        estilizarTabla(dgvReportes);
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlNorte = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlResumen = new javax.swing.JPanel();
        lblResumen = new javax.swing.JLabel();
        pnlFiltro = new javax.swing.JPanel();
        lblFil = new javax.swing.JLabel();
        cboFiltroMes = new javax.swing.JComboBox<>();
        lblTotVen = new javax.swing.JLabel();
        txtTotalVendido = new javax.swing.JTextField();
        lblCantPed = new javax.swing.JLabel();
        txtCantidadPedidos = new javax.swing.JTextField();
        lblTickProm = new javax.swing.JLabel();
        txtTicketPromedio = new javax.swing.JTextField();
        pnlCentro = new javax.swing.JPanel();
        pnlControlesSuperiores = new javax.swing.JPanel();
        pnlStock = new javax.swing.JPanel();
        lblProd = new javax.swing.JLabel();
        cmbProductos = new javax.swing.JComboBox<>();
        lblCant = new javax.swing.JLabel();
        txtCantidadAgregar = new javax.swing.JTextField();
        btnAgregarStock = new javax.swing.JButton();
        pnlBotonesReportes = new javax.swing.JPanel();
        btnReporteMasVendidos = new javax.swing.JButton();
        btnReporteStock = new javax.swing.JButton();
        btnReporteVentasMes = new javax.swing.JButton();
        btnReporteMetodoPago = new javax.swing.JButton();
        btnReporteClientes = new javax.swing.JButton();
        btnReporteEmpleados = new javax.swing.JButton();
        btnExportarTXT = new javax.swing.JButton();
        pnlTablaContainer = new javax.swing.JPanel();
        lblTabla = new javax.swing.JLabel();
        scrollReportes = new javax.swing.JScrollPane();
        dgvReportes = new javax.swing.JTable();

        setBackground(new java.awt.Color(200, 155, 90));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        setLayout(new java.awt.BorderLayout(12, 12));

        pnlNorte.setBackground(new java.awt.Color(200, 155, 90));
        pnlNorte.setLayout(new java.awt.BorderLayout(8, 8));

        lblTitulo.setFont(new java.awt.Font("Georgia", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(55, 32, 18));
        lblTitulo.setText("Panel del encargado");
        pnlNorte.add(lblTitulo, java.awt.BorderLayout.NORTH);

        pnlResumen.setBackground(new java.awt.Color(76, 47, 29));
        pnlResumen.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16));
        pnlResumen.setLayout(new java.awt.GridBagLayout());

        lblResumen.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblResumen.setForeground(new java.awt.Color(255, 255, 255));
        lblResumen.setText("RESUMEN DE VENTAS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(lblResumen, gridBagConstraints);

        pnlFiltro.setOpaque(false);
        pnlFiltro.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 0));

        lblFil.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFil.setForeground(new java.awt.Color(255, 255, 255));
        lblFil.setText("Filtrar por Mes:");
        pnlFiltro.add(lblFil);

        cboFiltroMes.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cboFiltroMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Octubre 2026 (Mes Actual)", "Septiembre 2026", "Agosto 2026", "Julio 2026", "Junio 2026", "Mayo 2026", "Abril 2026", "Marzo 2026", "Febrero 2026", "Enero 2026", "Todo el AÃ±o 2026 (Acumulado)" }));
        cboFiltroMes.setPreferredSize(new java.awt.Dimension(215, 26));
        cboFiltroMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFiltroMesActionPerformed(evt);
            }
        });
        pnlFiltro.add(cboFiltroMes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(pnlFiltro, gridBagConstraints);

        lblTotVen.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTotVen.setForeground(new java.awt.Color(255, 255, 255));
        lblTotVen.setText("Total vendido:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(lblTotVen, gridBagConstraints);

        txtTotalVendido.setEditable(false);
        txtTotalVendido.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalVendido.setColumns(10);
        txtTotalVendido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalVendido.setForeground(new java.awt.Color(76, 47, 29));
        txtTotalVendido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(txtTotalVendido, gridBagConstraints);

        lblCantPed.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCantPed.setForeground(new java.awt.Color(255, 255, 255));
        lblCantPed.setText("Cantidad pedidos:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(lblCantPed, gridBagConstraints);

        txtCantidadPedidos.setEditable(false);
        txtCantidadPedidos.setBackground(new java.awt.Color(255, 255, 255));
        txtCantidadPedidos.setColumns(10);
        txtCantidadPedidos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCantidadPedidos.setForeground(new java.awt.Color(76, 47, 29));
        txtCantidadPedidos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(txtCantidadPedidos, gridBagConstraints);

        lblTickProm.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTickProm.setForeground(new java.awt.Color(255, 255, 255));
        lblTickProm.setText("Ticket promedio:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(lblTickProm, gridBagConstraints);

        txtTicketPromedio.setEditable(false);
        txtTicketPromedio.setBackground(new java.awt.Color(255, 255, 255));
        txtTicketPromedio.setColumns(10);
        txtTicketPromedio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTicketPromedio.setForeground(new java.awt.Color(76, 47, 29));
        txtTicketPromedio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        pnlResumen.add(txtTicketPromedio, gridBagConstraints);

        pnlNorte.add(pnlResumen, java.awt.BorderLayout.CENTER);

        add(pnlNorte, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(200, 155, 90));
        pnlCentro.setLayout(new java.awt.BorderLayout(10, 10));

        pnlControlesSuperiores.setBackground(new java.awt.Color(200, 155, 90));
        pnlControlesSuperiores.setLayout(new java.awt.BorderLayout(8, 8));

        pnlStock.setBackground(new java.awt.Color(200, 155, 90));
        pnlStock.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 47, 29)), "Actualizar Stock de Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 11), new java.awt.Color(55, 32, 18))); // NOI18N
        pnlStock.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 6));

        lblProd.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblProd.setForeground(new java.awt.Color(55, 32, 18));
        lblProd.setText("Producto:");
        pnlStock.add(lblProd);

        cmbProductos.setPreferredSize(new java.awt.Dimension(240, 26));
        pnlStock.add(cmbProductos);

        lblCant.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCant.setForeground(new java.awt.Color(55, 32, 18));
        lblCant.setText("Cantidad a agregar:");
        pnlStock.add(lblCant);

        txtCantidadAgregar.setColumns(6);
        pnlStock.add(txtCantidadAgregar);

        btnAgregarStock.setBackground(new java.awt.Color(76, 47, 29));
        btnAgregarStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarStock.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarStock.setText("Agregar stock");
        btnAgregarStock.setFocusable(false);
        btnAgregarStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarStockActionPerformed(evt);
            }
        });
        pnlStock.add(btnAgregarStock);

        pnlControlesSuperiores.add(pnlStock, java.awt.BorderLayout.NORTH);

        pnlBotonesReportes.setBackground(new java.awt.Color(200, 155, 90));
        pnlBotonesReportes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));

        btnReporteMasVendidos.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteMasVendidos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteMasVendidos.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteMasVendidos.setText("Productos mÃ¡s vendidos");
        btnReporteMasVendidos.setFocusable(false);
        btnReporteMasVendidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteMasVendidosActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteMasVendidos);

        btnReporteStock.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteStock.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteStock.setText("Stock actual");
        btnReporteStock.setFocusable(false);
        btnReporteStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteStockActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteStock);

        btnReporteVentasMes.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteVentasMes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteVentasMes.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteVentasMes.setText("Ventas por mes");
        btnReporteVentasMes.setFocusable(false);
        btnReporteVentasMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteVentasMesActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteVentasMes);

        btnReporteMetodoPago.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteMetodoPago.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteMetodoPago.setText("Ventas por mÃ©todo de pago");
        btnReporteMetodoPago.setFocusable(false);
        btnReporteMetodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteMetodoPagoActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteMetodoPago);

        btnReporteClientes.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteClientes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteClientes.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteClientes.setText("Clientes frecuentes");
        btnReporteClientes.setFocusable(false);
        btnReporteClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteClientesActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteClientes);

        btnReporteEmpleados.setBackground(new java.awt.Color(76, 47, 29));
        btnReporteEmpleados.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReporteEmpleados.setForeground(new java.awt.Color(255, 255, 255));
        btnReporteEmpleados.setText("Pedidos por empleado");
        btnReporteEmpleados.setFocusable(false);
        btnReporteEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteEmpleadosActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnReporteEmpleados);

        btnExportarTXT.setBackground(new java.awt.Color(76, 47, 29));
        btnExportarTXT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportarTXT.setForeground(new java.awt.Color(255, 255, 255));
        btnExportarTXT.setText("Exportar Reporte TXT");
        btnExportarTXT.setFocusable(false);
        btnExportarTXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarTXTActionPerformed(evt);
            }
        });
        pnlBotonesReportes.add(btnExportarTXT);

        pnlControlesSuperiores.add(pnlBotonesReportes, java.awt.BorderLayout.SOUTH);

        pnlCentro.add(pnlControlesSuperiores, java.awt.BorderLayout.NORTH);

        pnlTablaContainer.setBackground(new java.awt.Color(200, 155, 90));
        pnlTablaContainer.setLayout(new java.awt.BorderLayout(6, 6));

        lblTabla.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTabla.setForeground(new java.awt.Color(55, 32, 18));
        lblTabla.setText("Productos mÃ¡s vendidos");
        pnlTablaContainer.add(lblTabla, java.awt.BorderLayout.NORTH);

        dgvReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrollReportes.setViewportView(dgvReportes);

        pnlTablaContainer.add(scrollReportes, java.awt.BorderLayout.CENTER);

        pnlCentro.add(pnlTablaContainer, java.awt.BorderLayout.CENTER);

        add(pnlCentro, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cboFiltroMesActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarResumenYReporte();
    }

    private void btnAgregarStockActionPerformed(java.awt.event.ActionEvent evt) {
        agregarStock();
    }

    private void btnReporteMasVendidosActionPerformed(java.awt.event.ActionEvent evt) {
        cargarProductosMasVendidos();
    }

    private void btnReporteStockActionPerformed(java.awt.event.ActionEvent evt) {
        cargarStockActual();
    }

    private void btnReporteVentasMesActionPerformed(java.awt.event.ActionEvent evt) {
        cargarVentasPorMes();
    }

    private void btnReporteMetodoPagoActionPerformed(java.awt.event.ActionEvent evt) {
        cargarVentasMetodoPago();
    }

    private void btnReporteClientesActionPerformed(java.awt.event.ActionEvent evt) {
        cargarClientesFrecuentes();
    }

    private void btnReporteEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {
        cargarPedidosPorEmpleado();
    }

    private void btnExportarTXTActionPerformed(java.awt.event.ActionEvent evt) {
        exportarReporteEjecutivo();
    }

    private int obtenerMesSeleccionado() {
        if (cboFiltroMes == null) return 10;
        int idx = cboFiltroMes.getSelectedIndex();
        if (idx == 10) return 0;
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
            default: return "Todo el AÃ±o 2026";
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
            JOptionPane.showMessageDialog(this, "Seleccione un producto.", "ValidaciÃ³n", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantStr = txtCantidadAgregar.getText().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad vÃ¡lida mayor a cero.", "ValidaciÃ³n", JOptionPane.WARNING_MESSAGE);
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
        lblTabla.setText("Productos mÃ¡s vendidos - " + obtenerNombreMesSeleccionado());
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
        String[] cols = {"Producto", "CategorÃ­a", "Stock Actual", "Proveedor", "Estado"};
        modeloReportes.setDataVector(new Object[][]{}, cols);
        aplicarEstiloHeader(dgvReportes);

        List<Object[]> datos = gestor.obtenerReporteStockActual();
        for (Object[] fila : datos) {
            modeloReportes.addRow(fila);
        }
    }

    public void cargarVentasPorMes() {
        tipoReporteActivo = 3;
        lblTabla.setText("Ventas agrupadas por mes (HistÃ³rico 2026)");
        String[] cols = {"Mes / PerÃ­odo", "Cantidad de Pedidos", "Total Vendido (S/)", "Ticket Promedio (S/)"};
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
        lblTabla.setText("Ventas por mÃ©todo de pago - " + obtenerNombreMesSeleccionado());
        String[] cols = {"MÃ©todo de Pago", "Cantidad de Pedidos", "Total Vendido (S/)"};
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
                "Reporte ejecutivo exportado con Ã©xito en:\n" + archivo.getAbsolutePath(),
                "Reporte Exportado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarStock;
    private javax.swing.JButton btnExportarTXT;
    private javax.swing.JButton btnReporteClientes;
    private javax.swing.JButton btnReporteEmpleados;
    private javax.swing.JButton btnReporteMasVendidos;
    private javax.swing.JButton btnReporteMetodoPago;
    private javax.swing.JButton btnReporteStock;
    private javax.swing.JButton btnReporteVentasMes;
    private javax.swing.JComboBox<String> cboFiltroMes;
    private javax.swing.JComboBox<String> cmbProductos;
    private javax.swing.JTable dgvReportes;
    private javax.swing.JLabel lblCant;
    private javax.swing.JLabel lblCantPed;
    private javax.swing.JLabel lblFil;
    private javax.swing.JLabel lblProd;
    private javax.swing.JLabel lblResumen;
    private javax.swing.JLabel lblTabla;
    private javax.swing.JLabel lblTickProm;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotVen;
    private javax.swing.JPanel pnlBotonesReportes;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlControlesSuperiores;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlNorte;
    private javax.swing.JPanel pnlResumen;
    private javax.swing.JPanel pnlStock;
    private javax.swing.JPanel pnlTablaContainer;
    private javax.swing.JScrollPane scrollReportes;
    private javax.swing.JTextField txtCantidadAgregar;
    private javax.swing.JTextField txtCantidadPedidos;
    private javax.swing.JTextField txtTicketPromedio;
    private javax.swing.JTextField txtTotalVendido;
    // End of variables declaration//GEN-END:variables
}
