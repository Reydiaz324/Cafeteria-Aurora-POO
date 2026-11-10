package vista;

import controlador.GestorCafeteria;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * FORMULARIO: FrmPrincipal
 * Ventana principal de bienvenida y acceso general con soporte de diseño visual en NetBeans (.form).
 */
public class FrmPrincipal extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private GestorCafeteria gestor;

    public FrmPrincipal() {
        this(new GestorCafeteria());
    }

    public FrmPrincipal(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        this.setSize(750, 480);
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlNorte = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        btnModuloPedido = new javax.swing.JButton();
        btnModuloEncargado = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        pnlSur = new javax.swing.JPanel();
        lblPie = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cafetería Aurora - Sistema de Gestión POO");
        setResizable(false);

        pnlNorte.setBackground(new java.awt.Color(76, 47, 29));
        pnlNorte.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        pnlNorte.setLayout(new java.awt.GridLayout(2, 1, 0, 6));

        lblTitulo.setFont(new java.awt.Font("Georgia", 1, 26)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(232, 195, 158));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("CAFETERÍA AURORA");
        pnlNorte.add(lblTitulo);

        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblSubtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitulo.setText("Sistema de Gestión y Punto de Venta (POS) - TECPRO Grupo 2");
        pnlNorte.add(lblSubtitulo);

        getContentPane().add(pnlNorte, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(245, 237, 224));
        pnlCentro.setBorder(javax.swing.BorderFactory.createEmptyBorder(35, 80, 35, 80));
        pnlCentro.setLayout(new java.awt.GridLayout(3, 1, 0, 18));

        btnModuloPedido.setBackground(new java.awt.Color(140, 90, 50));
        btnModuloPedido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModuloPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnModuloPedido.setText("1. MÓDULO DE ATENCIÓN DE PEDIDOS (BARISTA / POS)");
        btnModuloPedido.setPreferredSize(new java.awt.Dimension(380, 50));
        btnModuloPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModuloPedidoActionPerformed(evt);
            }
        });
        pnlCentro.add(btnModuloPedido);

        btnModuloEncargado.setBackground(new java.awt.Color(76, 47, 29));
        btnModuloEncargado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModuloEncargado.setForeground(new java.awt.Color(255, 255, 255));
        btnModuloEncargado.setText("2. PANEL DEL ENCARGADO (ADMINISTRACIÓN Y REPORTES)");
        btnModuloEncargado.setPreferredSize(new java.awt.Dimension(380, 50));
        btnModuloEncargado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModuloEncargadoActionPerformed(evt);
            }
        });
        pnlCentro.add(btnModuloEncargado);

        btnSalir.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnSalir.setText("Salir del Sistema");
        btnSalir.setPreferredSize(new java.awt.Dimension(380, 45));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        pnlCentro.add(btnSalir);

        getContentPane().add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlSur.setBackground(new java.awt.Color(76, 47, 29));
        pnlSur.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pnlSur.setLayout(new java.awt.FlowLayout());

        lblPie.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        lblPie.setForeground(new java.awt.Color(230, 215, 180));
        lblPie.setText("Cafetería Aurora v2.5 - Conexión SQL Server BD_Cafeteria_POO y Soporte Offline");
        pnlSur.add(lblPie);

        getContentPane().add(pnlSur, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModuloPedidoActionPerformed(java.awt.event.ActionEvent evt) {
        new FrmPedido(gestor).setVisible(true);
    }

    private void btnModuloEncargadoActionPerformed(java.awt.event.ActionEvent evt) {
        new FrmLogin(gestor).setVisible(true);
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        int r = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del sistema?", "Salir", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FrmPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnModuloEncargado;
    private javax.swing.JButton btnModuloPedido;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblPie;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlNorte;
    private javax.swing.JPanel pnlSur;
    // End of variables declaration//GEN-END:variables
}
