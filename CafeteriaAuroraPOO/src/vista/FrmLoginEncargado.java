package vista;

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * FORMULARIO DE ACCESO: FrmLoginEncargado
 * Cuenta con archivo .form nativo para NetBeans GUI Builder ([Design]).
 */
public class FrmLoginEncargado extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final String CLAVE_ENCARGADO = "1234";

    private boolean autenticado = false;

    public FrmLoginEncargado(Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public FrmLoginEncargado() {
        this(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        pnlCenter = new javax.swing.JPanel();
        lblMsg = new javax.swing.JLabel();
        txtContrasena = new javax.swing.JPasswordField();
        pnlBotones = new javax.swing.JPanel();
        btnIngresar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Acceso Encargado");
        setModal(true);
        setResizable(false);

        pnlHeader.setBackground(new java.awt.Color(76, 47, 29));
        pnlHeader.setLayout(new java.awt.FlowLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setText("INICIO DE SESIÓN - ENCARGADO");
        lblHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlHeader.add(lblHeader);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenter.setBackground(new java.awt.Color(200, 155, 90));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        pnlCenter.setLayout(new java.awt.GridBagLayout());

        lblMsg.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMsg.setForeground(new java.awt.Color(55, 32, 18));
        lblMsg.setText("Ingrese la contraseña del encargado:");
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        pnlCenter.add(lblMsg, gridBagConstraints);

        txtContrasena.setColumns(16);
        txtContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContrasenaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        pnlCenter.add(txtContrasena, gridBagConstraints);

        getContentPane().add(pnlCenter, java.awt.BorderLayout.CENTER);

        pnlBotones.setBackground(new java.awt.Color(200, 155, 90));
        pnlBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 5));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 8));

        btnIngresar.setBackground(new java.awt.Color(76, 47, 29));
        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.setFocusable(false);
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnIngresar);

        btnCancelar.setBackground(new java.awt.Color(76, 47, 29));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCancelar);

        getContentPane().add(pnlBotones, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        validarAcceso();
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        autenticado = false;
        dispose();
    }

    private void txtContrasenaActionPerformed(java.awt.event.ActionEvent evt) {
        validarAcceso();
    }

    private void validarAcceso() {
        String pass = new String(txtContrasena.getPassword()).trim();
        if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese la contraseña del encargado.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            txtContrasena.requestFocus();
            return;
        }

        if (CLAVE_ENCARGADO.equals(pass) || "00000001".equals(pass)) {
            autenticado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Contraseña incorrecta. Solo el encargado puede acceder a este panel.",
                "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPasswordField txtContrasena;
    // End of variables declaration//GEN-END:variables
}
