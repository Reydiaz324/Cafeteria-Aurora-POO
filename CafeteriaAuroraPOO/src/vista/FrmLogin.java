package vista;

import controlador.GestorCafeteria;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * FORMULARIO: FrmLogin
 * Ventana de inicio de sesión con soporte de diseño visual en NetBeans (.form).
 */
public class FrmLogin extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private GestorCafeteria gestor;

    public FrmLogin() {
        this(new GestorCafeteria());
    }

    public FrmLogin(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        this.setSize(420, 280);
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCabecera = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlCampos = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        pnlBotones = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnIngresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Acceso de Administración - Cafetería Aurora");
        setResizable(false);

        pnlCabecera.setBackground(new java.awt.Color(76, 47, 29));
        pnlCabecera.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlCabecera.setLayout(new java.awt.FlowLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("INICIAR SESIÓN - ENCARGADO");
        pnlCabecera.add(lblTitulo);

        getContentPane().add(pnlCabecera, java.awt.BorderLayout.NORTH);

        pnlCampos.setBackground(new java.awt.Color(245, 237, 224));
        pnlCampos.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 25, 20, 25));
        pnlCampos.setLayout(new java.awt.GridLayout(4, 1, 0, 6));

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblUsuario.setText("Usuario del Encargado:");
        pnlCampos.add(lblUsuario);

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtUsuario.setText("admin");
        pnlCampos.add(txtUsuario);

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPassword.setText("Contraseña de Seguridad:");
        pnlCampos.add(lblPassword);

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPassword.setText("aurora2026");
        pnlCampos.add(txtPassword);

        getContentPane().add(pnlCampos, java.awt.BorderLayout.CENTER);

        pnlBotones.setBackground(new java.awt.Color(245, 237, 224));
        pnlBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 15, 15));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCancelar);

        btnIngresar.setBackground(new java.awt.Color(140, 90, 50));
        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar al Panel");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnIngresar);

        getContentPane().add(pnlBotones, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        String pass = new String(txtPassword.getPassword()).trim();
        if ("aurora2026".equals(pass) || "admin".equalsIgnoreCase(pass)) {
            this.dispose();
            new FrmEncargado(gestor).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Contraseña incorrecta.\nIngrese: aurora2026",
                    "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCabecera;
    private javax.swing.JPanel pnlCampos;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
