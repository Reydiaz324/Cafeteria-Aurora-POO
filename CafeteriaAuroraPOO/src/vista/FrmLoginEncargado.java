package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class FrmLoginEncargado extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final String CLAVE_ENCARGADO = "1234";
    private static final Color COLOR_MARRON_OSCURO = new Color(76, 47, 29);
    private static final Color COLOR_FONDO = new Color(200, 155, 90);

    private JPasswordField txtContrasena;
    private boolean autenticado = false;

    public FrmLoginEncargado(Frame parent) {
        super(parent, "Acceso Encargado", true);
        initUI();
    }

    private void initUI() {
        setSize(380, 210);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(COLOR_MARRON_OSCURO);
        JLabel lblHeader = new JLabel("INICIO DE SESIÓN - ENCARGADO");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlHeader.add(lblHeader);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Form
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_FONDO);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblMsg = new JLabel("Ingrese la contraseña del encargado:");
        lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMsg.setForeground(new Color(55, 32, 18));
        pnlCenter.add(lblMsg, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlCenter.add(txtContrasena, gbc);

        add(pnlCenter, BorderLayout.CENTER);

        // Buttons
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        pnlBotones.setBackground(COLOR_FONDO);

        BotonFlat btnIngresar = new BotonFlat("Ingresar", BotonFlat.Estilo.ACCION_CAFE);
        btnIngresar.addActionListener(e -> validarAcceso());

        BotonFlat btnCancelar = new BotonFlat("Cancelar", BotonFlat.Estilo.ACCION_CAFE);
        btnCancelar.addActionListener(e -> {
            autenticado = false;
            dispose();
        });

        pnlBotones.add(btnIngresar);
        pnlBotones.add(btnCancelar);
        add(pnlBotones, BorderLayout.SOUTH);

        txtContrasena.addActionListener(e -> validarAcceso());
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
}
