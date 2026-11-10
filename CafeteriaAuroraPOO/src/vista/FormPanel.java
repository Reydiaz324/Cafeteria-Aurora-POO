package vista;

import controlador.GestorCafeteria;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * VENTANA PRINCIPAL: FormPanel
 * Cuenta con archivo .form para habilitar la pestaña [Design] en NetBeans.
 * Permite mover con el mouse los botones del menú lateral, cabecera y accesos directos.
 */
public class FormPanel extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final Color COLOR_FONDO_CENTRAL = new Color(200, 155, 90);
    public static final Color COLOR_BARRA_MENU = new Color(76, 47, 29);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;
    private CardLayout cardLayout;

    // Pantallas hijas integradas en CardLayout
    private PanelPedido panelPedido;
    private PanelEncargado panelEncargado;

    public FormPanel() {
        this(new GestorCafeteria());
    }

    public FormPanel(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initComponents();
        personalizarVistas();
        setSize(1240, 720);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
    }

    private void personalizarVistas() {
        cardLayout = (CardLayout) panelogo.getLayout();
        panelPedido = new PanelPedido(gestor);
        panelEncargado = new PanelEncargado(gestor);

        panelogo.add(panelPedido, "PEDIDO");
        panelogo.add(panelEncargado, "ENCARGADO");

        mostrarInicio();
    }

    public void mostrarInicio() {
        if (cardLayout != null) {
            cardLayout.show(panelogo, "INICIO");
        }
        lblEstadoSesion.setText("Modo: Inicio / Selección de Módulo");
    }

    public void mostrarPedido() {
        if (cardLayout != null) {
            cardLayout.show(panelogo, "PEDIDO");
        }
        lblEstadoSesion.setText("Sesión Activa: Trabajador / Barista (Atención de Pedidos)");
        if (panelPedido != null) {
            panelPedido.cargarProductos(0);
        }
    }

    public void mostrarEncargado() {
        FrmLoginEncargado login = new FrmLoginEncargado(this);
        login.setVisible(true);

        if (!login.isAutenticado()) {
            return;
        }

        if (cardLayout != null) {
            cardLayout.show(panelogo, "ENCARGADO");
        }
        lblEstadoSesion.setText("Sesión Activa: Administrador / Mariana Vega (Encargado)");
        if (panelEncargado != null) {
            panelEncargado.cargarResumenGeneral();
            panelEncargado.cargarComboBoxProductos();
            panelEncargado.cargarProductosMasVendidos();
        }
    }

    private void confirmarSalida() {
        int r = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea salir del sistema?",
                "Confirmar Salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            try {
                gestor.persistirTodoEnArchivos();
            } catch (Exception ignored) {}
            dispose();
            System.exit(0);
        }
    }

    public GestorCafeteria getGestor() {
        return gestor;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelBarra = new javax.swing.JPanel();
        lblTituloSistema = new javax.swing.JLabel();
        lblEstadoSesion = new javax.swing.JLabel();
        Panelmenu = new javax.swing.JPanel();
        pnlMenuHeader = new javax.swing.JPanel();
        lblMenuLogo = new javax.swing.JLabel();
        lblMenuSub = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnpedido = new javax.swing.JButton();
        btnEncargado = new javax.swing.JButton();
        lblEspacioMenu = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();
        panelogo = new javax.swing.JPanel();
        panelBienvenida = new javax.swing.JPanel();
        pnlCardCentral = new javax.swing.JPanel();
        lblNom = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        lblInstruccion = new javax.swing.JLabel();
        pnlAccesoDirecto = new javax.swing.JPanel();
        btnAbrirPedido = new javax.swing.JButton();
        btnAbrirEncargado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cafetería Aurora");
        setMinimumSize(new java.awt.Dimension(1100, 650));

        PanelBarra.setBackground(new java.awt.Color(76, 47, 29));
        PanelBarra.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        PanelBarra.setPreferredSize(new java.awt.Dimension(1240, 36));
        PanelBarra.setLayout(new java.awt.BorderLayout());

        lblTituloSistema.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTituloSistema.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloSistema.setText("CAFETERÍA AURORA");
        PanelBarra.add(lblTituloSistema, java.awt.BorderLayout.WEST);

        lblEstadoSesion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblEstadoSesion.setForeground(new java.awt.Color(230, 215, 195));
        lblEstadoSesion.setText("Modo: Inicio / Selección de Módulo");
        PanelBarra.add(lblEstadoSesion, java.awt.BorderLayout.EAST);

        getContentPane().add(PanelBarra, java.awt.BorderLayout.NORTH);

        Panelmenu.setBackground(new java.awt.Color(76, 47, 29));
        Panelmenu.setPreferredSize(new java.awt.Dimension(210, 680));
        Panelmenu.setLayout(new java.awt.BorderLayout());

        pnlMenuHeader.setBackground(new java.awt.Color(76, 47, 29));
        pnlMenuHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 10, 20, 10));
        pnlMenuHeader.setLayout(new java.awt.GridLayout(2, 1));

        lblMenuLogo.setFont(new java.awt.Font("Georgia", 1, 19)); // NOI18N
        lblMenuLogo.setForeground(new java.awt.Color(245, 230, 210));
        lblMenuLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenuLogo.setText("CAFETERÍA");
        pnlMenuHeader.add(lblMenuLogo);

        lblMenuSub.setFont(new java.awt.Font("Georgia", 1, 19)); // NOI18N
        lblMenuSub.setForeground(new java.awt.Color(225, 185, 130));
        lblMenuSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenuSub.setText("AURORA");
        pnlMenuHeader.add(lblMenuSub);

        Panelmenu.add(pnlMenuHeader, java.awt.BorderLayout.NORTH);

        pnlBotones.setBackground(new java.awt.Color(76, 47, 29));
        pnlBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlBotones.setLayout(new java.awt.GridLayout(5, 1, 0, 8));

        btnInicio.setBackground(new java.awt.Color(76, 47, 29));
        btnInicio.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnInicio.setForeground(new java.awt.Color(255, 255, 255));
        btnInicio.setText("  Inicio");
        btnInicio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });
        pnlBotones.add(btnInicio);

        btnpedido.setBackground(new java.awt.Color(76, 47, 29));
        btnpedido.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnpedido.setForeground(new java.awt.Color(255, 255, 255));
        btnpedido.setText("  Pedido");
        btnpedido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnpedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpedidoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnpedido);

        btnEncargado.setBackground(new java.awt.Color(76, 47, 29));
        btnEncargado.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEncargado.setForeground(new java.awt.Color(255, 255, 255));
        btnEncargado.setText("  Encargado");
        btnEncargado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEncargado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncargadoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnEncargado);
        pnlBotones.add(lblEspacioMenu);

        btnSalir.setBackground(new java.awt.Color(76, 47, 29));
        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("  Cerrar");
        btnSalir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        pnlBotones.add(btnSalir);

        Panelmenu.add(pnlBotones, java.awt.BorderLayout.CENTER);

        lblVersion.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        lblVersion.setForeground(new java.awt.Color(180, 150, 120));
        lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVersion.setText("v2.5 POO Final");
        lblVersion.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        Panelmenu.add(lblVersion, java.awt.BorderLayout.SOUTH);

        getContentPane().add(Panelmenu, java.awt.BorderLayout.WEST);

        panelogo.setBackground(new java.awt.Color(200, 155, 90));
        panelogo.setLayout(new java.awt.CardLayout());

        panelBienvenida.setBackground(new java.awt.Color(200, 155, 90));
        panelBienvenida.setLayout(new java.awt.GridBagLayout());

        pnlCardCentral.setBackground(new java.awt.Color(245, 235, 220));
        pnlCardCentral.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(76, 47, 29), 3), javax.swing.BorderFactory.createEmptyBorder(30, 40, 40, 30)));
        pnlCardCentral.setLayout(new java.awt.GridLayout(5, 1, 0, 14));

        lblNom.setFont(new java.awt.Font("Georgia", 1, 26)); // NOI18N
        lblNom.setForeground(new java.awt.Color(55, 32, 18));
        lblNom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNom.setText("CAFETERÍA AURORA");
        pnlCardCentral.add(lblNom);

        lblSub.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblSub.setForeground(new java.awt.Color(51, 51, 51));
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("Sistema de Ventas, Almacén y Control de Operaciones");
        pnlCardCentral.add(lblSub);

        lblInstruccion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblInstruccion.setForeground(new java.awt.Color(102, 102, 102));
        lblInstruccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInstruccion.setText("Seleccione una opción en el menú lateral para comenzar:");
        pnlCardCentral.add(lblInstruccion);

        pnlAccesoDirecto.setOpaque(false);
        pnlAccesoDirecto.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        btnAbrirPedido.setBackground(new java.awt.Color(76, 47, 29));
        btnAbrirPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAbrirPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirPedido.setText("Abrir Módulo de Pedidos");
        btnAbrirPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirPedidoActionPerformed(evt);
            }
        });
        pnlAccesoDirecto.add(btnAbrirPedido);

        btnAbrirEncargado.setBackground(new java.awt.Color(76, 47, 29));
        btnAbrirEncargado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAbrirEncargado.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirEncargado.setText("Panel del Encargado");
        btnAbrirEncargado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirEncargadoActionPerformed(evt);
            }
        });
        pnlAccesoDirecto.add(btnAbrirEncargado);

        pnlCardCentral.add(pnlAccesoDirecto);

        panelBienvenida.add(pnlCardCentral, new java.awt.GridBagConstraints());

        panelogo.add(panelBienvenida, "INICIO");

        getContentPane().add(panelogo, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarInicio();
    }

    private void btnpedidoActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarPedido();
    }

    private void btnEncargadoActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarEncargado();
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        confirmarSalida();
    }

    private void btnAbrirPedidoActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarPedido();
    }

    private void btnAbrirEncargadoActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarEncargado();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FormPanel().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBarra;
    private javax.swing.JPanel Panelmenu;
    private javax.swing.JButton btnAbrirEncargado;
    private javax.swing.JButton btnAbrirPedido;
    private javax.swing.JButton btnEncargado;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnpedido;
    private javax.swing.JLabel lblEspacioMenu;
    private javax.swing.JLabel lblEstadoSesion;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JLabel lblMenuLogo;
    private javax.swing.JLabel lblMenuSub;
    private javax.swing.JLabel lblNom;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTituloSistema;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JPanel panelBienvenida;
    private javax.swing.JPanel panelogo;
    private javax.swing.JPanel pnlAccesoDirecto;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCardCentral;
    private javax.swing.JPanel pnlMenuHeader;
    // End of variables declaration//GEN-END:variables
}
