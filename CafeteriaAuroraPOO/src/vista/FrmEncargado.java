package vista;

import controlador.GestorCafeteria;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;

/**
 * FORMULARIO: FrmEncargado
 * Panel administrativo, métricas KPI y reportes de ventas con soporte visual en NetBeans (.form).
 */
public class FrmEncargado extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private GestorCafeteria gestor;
    private DefaultTableModel modeloReportes;

    public FrmEncargado() {
        this(new GestorCafeteria());
    }

    public FrmEncargado(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        personalizarComponentes();
        cargarComboProductosStock();
        actualizarKpisYReporte(0);
        this.setSize(1100, 720);
        this.setLocationRelativeTo(null);
    }

    private void personalizarComponentes() {
        modeloReportes = new DefaultTableModel(new String[]{"Mes / Período", "Cantidad de Pedidos", "Total Vendido (S/)", "Ticket Promedio (S/)"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblReportes.setModel(modeloReportes);
    }

    private void cargarComboProductosStock() {
        cboProductoStock.removeAllItems();
        if (gestor != null && gestor.getProductos() != null) {
            for (Producto p : gestor.getProductos()) {
                cboProductoStock.addItem(p.getIdProducto() + " - " + p.getNombreProducto() + " (Stock: " + p.getStock() + ")");
            }
        }
    }

    private int obtenerMesSeleccionado() {
        int idx = cboMeses.getSelectedIndex();
        if (idx == 0) return 0; // 0 = Todo el año 2026
        return 11 - idx; // 1 = Octubre (10), 2 = Sept (9), ..., 10 = Enero (1)
    }

    private void actualizarKpisYReporte(int mesIndex) {
        if (gestor == null) return;

        int mes = obtenerMesSeleccionado();
        double[] resumen = gestor.obtenerResumenGeneralVentas(mes, 2026);

        lblKpiTotal.setText(String.format("S/ %.2f", resumen[0]));
        lblKpiPedidos.setText(String.format("%,d", (int) resumen[1]));
        lblKpiTicket.setText(String.format("S/ %.2f", resumen[2]));

        mostrarReporteVentasMes();
    }

    private void mostrarReporteVentasMes() {
        String[] cols = {"Mes / Período", "Cantidad de Pedidos", "Total Vendido (S/)", "Ticket Promedio (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);

        List<Object[]> filas = gestor.obtenerReporteVentasPorMes();
        for (Object[] f : filas) {
            modeloReportes.addRow(f);
        }
    }

    private void mostrarReporteMetodosPago() {
        String[] cols = {"Método de Pago", "Cantidad de Transacciones", "Total Recaudado (S/)"};
        modeloReportes.setDataVector(new Object[][]{}, cols);

        int mes = obtenerMesSeleccionado();
        List<Object[]> filas = gestor.obtenerReporteVentasMetodoPago(mes, 2026);
        for (Object[] f : filas) {
            modeloReportes.addRow(f);
        }
    }

    private void mostrarReporteStock() {
        String[] cols = {"ID", "Producto", "Categoría", "Precio (S/)", "Stock Actual", "Estado"};
        modeloReportes.setDataVector(new Object[][]{}, cols);

        List<Object[]> filas = gestor.obtenerReporteStockActual();
        for (Object[] f : filas) {
            modeloReportes.addRow(f);
        }
    }

    private void reabastecerStock() {
        int idx = cboProductoStock.getSelectedIndex();
        if (idx < 0 || idx >= gestor.getProductos().size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto a reabastecer.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantStr = txtCantidadStock.getText().trim();
        int cant;
        try {
            cant = Integer.parseInt(cantStr);
            if (cant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida mayor a 0.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Producto p = gestor.getProductos().get(idx);
            gestor.agregarStockProducto(p.getIdProducto(), cant);

            JOptionPane.showMessageDialog(this,
                    "Se agregaron " + cant + " unidades a '" + p.getNombreProducto() + "'.\nNuevo Stock: " + p.getStock(),
                    "Stock Actualizado", JOptionPane.INFORMATION_MESSAGE);

            cargarComboProductosStock();
            cboProductoStock.setSelectedIndex(idx);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al reabastecer stock: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlNorte = new javax.swing.JPanel();
        lblTituloPrincipal = new javax.swing.JLabel();
        pnlKpis = new javax.swing.JPanel();
        cardTotal = new javax.swing.JPanel();
        lblCard1Tit = new javax.swing.JLabel();
        lblKpiTotal = new javax.swing.JLabel();
        cardPedidos = new javax.swing.JPanel();
        lblCard2Tit = new javax.swing.JLabel();
        lblKpiPedidos = new javax.swing.JLabel();
        cardTicket = new javax.swing.JPanel();
        lblCard3Tit = new javax.swing.JLabel();
        lblKpiTicket = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        pnlBarraFiltros = new javax.swing.JPanel();
        lblFiltroMes = new javax.swing.JLabel();
        cboMeses = new javax.swing.JComboBox<>();
        btnReporteVentas = new javax.swing.JButton();
        btnReportePagos = new javax.swing.JButton();
        btnReporteStock = new javax.swing.JButton();
        jScrollPaneReportes = new javax.swing.JScrollPane();
        tblReportes = new javax.swing.JTable();
        pnlSur = new javax.swing.JPanel();
        pnlReabastecer = new javax.swing.JPanel();
        lblStockTit = new javax.swing.JLabel();
        cboProductoStock = new javax.swing.JComboBox<>();
        txtCantidadStock = new javax.swing.JTextField();
        btnReabastecer = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Panel del Encargado - Cafetería Aurora");

        pnlNorte.setBackground(new java.awt.Color(76, 47, 29));
        pnlNorte.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        pnlNorte.setLayout(new java.awt.BorderLayout(0, 10));

        lblTituloPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTituloPrincipal.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloPrincipal.setText("CAFETERÍA AURORA - PANEL DE ADMINISTRACIÓN Y REPORTES");
        pnlNorte.add(lblTituloPrincipal, java.awt.BorderLayout.NORTH);

        pnlKpis.setOpaque(false);
        pnlKpis.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        cardTotal.setBackground(new java.awt.Color(140, 90, 50));
        cardTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        cardTotal.setLayout(new java.awt.GridLayout(2, 1));

        lblCard1Tit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCard1Tit.setForeground(new java.awt.Color(245, 230, 200));
        lblCard1Tit.setText("TOTAL VENDIDO");
        cardTotal.add(lblCard1Tit);

        lblKpiTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblKpiTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblKpiTotal.setText("S/ 0.00");
        cardTotal.add(lblKpiTotal);

        pnlKpis.add(cardTotal);

        cardPedidos.setBackground(new java.awt.Color(140, 90, 50));
        cardPedidos.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        cardPedidos.setLayout(new java.awt.GridLayout(2, 1));

        lblCard2Tit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCard2Tit.setForeground(new java.awt.Color(245, 230, 200));
        lblCard2Tit.setText("PEDIDOS ATENDIDOS");
        cardPedidos.add(lblCard2Tit);

        lblKpiPedidos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblKpiPedidos.setForeground(new java.awt.Color(255, 255, 255));
        lblKpiPedidos.setText("0");
        cardPedidos.add(lblKpiPedidos);

        pnlKpis.add(cardPedidos);

        cardTicket.setBackground(new java.awt.Color(140, 90, 50));
        cardTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        cardTicket.setLayout(new java.awt.GridLayout(2, 1));

        lblCard3Tit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCard3Tit.setForeground(new java.awt.Color(245, 230, 200));
        lblCard3Tit.setText("TICKET PROMEDIO");
        cardTicket.add(lblCard3Tit);

        lblKpiTicket.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblKpiTicket.setForeground(new java.awt.Color(255, 255, 255));
        lblKpiTicket.setText("S/ 0.00");
        cardTicket.add(lblKpiTicket);

        pnlKpis.add(cardTicket);

        pnlNorte.add(pnlKpis, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlNorte, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(245, 237, 224));
        pnlCentro.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pnlCentro.setLayout(new java.awt.BorderLayout(0, 8));

        pnlBarraFiltros.setBackground(new java.awt.Color(245, 237, 224));
        pnlBarraFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        lblFiltroMes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFiltroMes.setText("Mes / Período:");
        pnlBarraFiltros.add(lblFiltroMes);

        cboMeses.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cboMeses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Todo el Año 2026 --", "Octubre 2026", "Septiembre 2026", "Agosto 2026", "Julio 2026", "Junio 2026", "Mayo 2026", "Abril 2026", "Marzo 2026", "Febrero 2026", "Enero 2026" }));
        cboMeses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMesesActionPerformed(evt);
            }
        });
        pnlBarraFiltros.add(cboMeses);

        btnReporteVentas.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnReporteVentas.setText("Ventas por Mes");
        btnReporteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteVentasActionPerformed(evt);
            }
        });
        pnlBarraFiltros.add(btnReporteVentas);

        btnReportePagos.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnReportePagos.setText("Métodos de Pago");
        btnReportePagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePagosActionPerformed(evt);
            }
        });
        pnlBarraFiltros.add(btnReportePagos);

        btnReporteStock.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnReporteStock.setText("Inventario Actual");
        btnReporteStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteStockActionPerformed(evt);
            }
        });
        pnlBarraFiltros.add(btnReporteStock);

        pnlCentro.add(pnlBarraFiltros, java.awt.BorderLayout.NORTH);

        tblReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mes / Período", "Cantidad de Pedidos", "Total Vendido (S/)", "Ticket Promedio (S/)"
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
        tblReportes.setRowHeight(24);
        jScrollPaneReportes.setViewportView(tblReportes);

        pnlCentro.add(jScrollPaneReportes, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlSur.setBackground(new java.awt.Color(76, 47, 29));
        pnlSur.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pnlSur.setLayout(new java.awt.BorderLayout());

        pnlReabastecer.setOpaque(false);
        pnlReabastecer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));

        lblStockTit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStockTit.setForeground(new java.awt.Color(255, 255, 255));
        lblStockTit.setText("Reabastecer Stock:");
        pnlReabastecer.add(lblStockTit);

        cboProductoStock.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cboProductoStock.setPreferredSize(new java.awt.Dimension(240, 26));
        pnlReabastecer.add(cboProductoStock);

        txtCantidadStock.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtCantidadStock.setText("20");
        txtCantidadStock.setPreferredSize(new java.awt.Dimension(50, 26));
        pnlReabastecer.add(txtCantidadStock);

        btnReabastecer.setBackground(new java.awt.Color(140, 90, 50));
        btnReabastecer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReabastecer.setForeground(new java.awt.Color(255, 255, 255));
        btnReabastecer.setText("+ Aumentar Stock");
        btnReabastecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReabastecerActionPerformed(evt);
            }
        });
        pnlReabastecer.add(btnReabastecer);

        pnlSur.add(pnlReabastecer, java.awt.BorderLayout.WEST);

        btnCerrar.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        pnlSur.add(btnCerrar, java.awt.BorderLayout.EAST);

        getContentPane().add(pnlSur, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboMesesActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarKpisYReporte(cboMeses.getSelectedIndex());
    }

    private void btnReporteVentasActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarReporteVentasMes();
    }

    private void btnReportePagosActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarReporteMetodosPago();
    }

    private void btnReporteStockActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarReporteStock();
    }

    private void btnReabastecerActionPerformed(java.awt.event.ActionEvent evt) {
        reabastecerStock();
    }

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FrmEncargado().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnReabastecer;
    private javax.swing.JButton btnReportePagos;
    private javax.swing.JButton btnReporteStock;
    private javax.swing.JButton btnReporteVentas;
    private javax.swing.JPanel cardPedidos;
    private javax.swing.JPanel cardTicket;
    private javax.swing.JPanel cardTotal;
    private javax.swing.JComboBox<String> cboMeses;
    private javax.swing.JComboBox<String> cboProductoStock;
    private javax.swing.JScrollPane jScrollPaneReportes;
    private javax.swing.JLabel lblCard1Tit;
    private javax.swing.JLabel lblCard2Tit;
    private javax.swing.JLabel lblCard3Tit;
    private javax.swing.JLabel lblFiltroMes;
    private javax.swing.JLabel lblKpiPedidos;
    private javax.swing.JLabel lblKpiTicket;
    private javax.swing.JLabel lblKpiTotal;
    private javax.swing.JLabel lblStockTit;
    private javax.swing.JLabel lblTituloPrincipal;
    private javax.swing.JPanel pnlBarraFiltros;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlKpis;
    private javax.swing.JPanel pnlNorte;
    private javax.swing.JPanel pnlReabastecer;
    private javax.swing.JPanel pnlSur;
    private javax.swing.JTable tblReportes;
    private javax.swing.JTextField txtCantidadStock;
    // End of variables declaration//GEN-END:variables
}
