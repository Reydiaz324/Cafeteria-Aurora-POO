package vista;

import controlador.GestorCafeteria;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FormPanel extends JFrame {
    private static final long serialVersionUID = 1L;

    // Paleta exacta de C#
    public static final Color COLOR_FONDO_CENTRAL = new Color(200, 155, 90);
    public static final Color COLOR_BARRA_MENU = new Color(76, 47, 29);
    public static final Color COLOR_TEXTO_TITULO = new Color(55, 32, 18);

    private final transient GestorCafeteria gestor;

    // Componentes del layout C#
    private JPanel PanelBarra;
    private JPanel Panelmenu;
    private JPanel panelogo;
    private CardLayout cardLayout;

    // Pantallas hijas
    private PanelPedido panelPedido;
    private PanelEncargado panelEncargado;
    private JPanel panelBienvenida;

    // Botones del menú lateral usando BotonFlat
    private BotonFlat btnInicio;
    private BotonFlat btnpedido;
    private BotonFlat btnEncargado;
    private BotonFlat btnPOSVisual;
    private BotonFlat btnSalir;

    private JLabel lblEstadoSesion;

    public FormPanel(GestorCafeteria gestor) {
        this.gestor = gestor != null ? gestor : new GestorCafeteria();
        initUI();
    }

    public FormPanel() {
        this(new GestorCafeteria());
    }

    private void initUI() {
        setTitle("Cafetería Aurora");
        setSize(1240, 720);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(COLOR_FONDO_CENTRAL);
        setContentPane(contentPane);

        // 1. PanelBarra (Barra superior - Top Bar)
        PanelBarra = new JPanel(new BorderLayout());
        PanelBarra.setBackground(COLOR_BARRA_MENU);
        PanelBarra.setPreferredSize(new Dimension(1240, 36));
        PanelBarra.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        JLabel lblTituloSistema = new JLabel("CAFETERÍA AURORA");
        lblTituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloSistema.setForeground(Color.WHITE);
        PanelBarra.add(lblTituloSistema, BorderLayout.WEST);

        lblEstadoSesion = new JLabel("Modo: Inicio / Selección de Módulo");
        lblEstadoSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstadoSesion.setForeground(new Color(230, 215, 195));
        PanelBarra.add(lblEstadoSesion, BorderLayout.EAST);

        contentPane.add(PanelBarra, BorderLayout.NORTH);

        // 2. Panelmenu (Menú lateral izquierdo de 210px - Left Sidebar)
        Panelmenu = new JPanel();
        Panelmenu.setLayout(new BorderLayout());
        Panelmenu.setBackground(COLOR_BARRA_MENU);
        Panelmenu.setPreferredSize(new Dimension(210, 680));

        // Header del menú
        JPanel pnlMenuHeader = new JPanel(new GridLayout(2, 1));
        pnlMenuHeader.setBackground(COLOR_BARRA_MENU);
        pnlMenuHeader.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblMenuLogo = new JLabel("CAFETERÍA", SwingConstants.CENTER);
        lblMenuLogo.setFont(new Font("Georgia", Font.BOLD, 19));
        lblMenuLogo.setForeground(new Color(245, 230, 210));

        JLabel lblMenuSub = new JLabel("AURORA", SwingConstants.CENTER);
        lblMenuSub.setFont(new Font("Georgia", Font.BOLD, 19));
        lblMenuSub.setForeground(new Color(225, 185, 130));

        pnlMenuHeader.add(lblMenuLogo);
        pnlMenuHeader.add(lblMenuSub);
        Panelmenu.add(pnlMenuHeader, BorderLayout.NORTH);

        // Botones del menú (Idénticos al C#)
        JPanel pnlBotones = new JPanel(new GridLayout(6, 1, 0, 6));
        pnlBotones.setBackground(COLOR_BARRA_MENU);
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnInicio = new BotonFlat("  Inicio", BotonFlat.Estilo.MENU_LATERAL);
        btnpedido = new BotonFlat("  Pedido", BotonFlat.Estilo.MENU_LATERAL);
        btnEncargado = new BotonFlat("  Encargado", BotonFlat.Estilo.MENU_LATERAL);
        btnPOSVisual = new BotonFlat("  POS Visual", BotonFlat.Estilo.MENU_LATERAL);
        btnSalir = new BotonFlat("  Cerrar", BotonFlat.Estilo.MENU_LATERAL);

        btnInicio.addActionListener(e -> mostrarInicio());
        btnpedido.addActionListener(e -> mostrarPedido());
        btnEncargado.addActionListener(e -> mostrarEncargado());
        btnPOSVisual.addActionListener(e -> new FrmPOSVisual(gestor).setVisible(true));
        btnSalir.addActionListener(e -> confirmarSalida());

        pnlBotones.add(btnInicio);
        pnlBotones.add(btnpedido);
        pnlBotones.add(btnEncargado);
        pnlBotones.add(btnPOSVisual);
        pnlBotones.add(new JLabel("")); // espaciador
        pnlBotones.add(btnSalir);

        Panelmenu.add(pnlBotones, BorderLayout.CENTER);

        // Footer del menú
        JLabel lblVersion = new JLabel("v2.5 POO Final", SwingConstants.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblVersion.setForeground(new Color(180, 150, 120));
        lblVersion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        Panelmenu.add(lblVersion, BorderLayout.SOUTH);

        contentPane.add(Panelmenu, BorderLayout.WEST);

        // 3. panelogo (Contenedor central con CardLayout)
        cardLayout = new CardLayout();
        panelogo = new JPanel(cardLayout);
        panelogo.setBackground(COLOR_FONDO_CENTRAL);

        // Crear vistas
        panelBienvenida = crearPanelBienvenida();
        panelPedido = new PanelPedido(gestor);
        panelEncargado = new PanelEncargado(gestor);

        panelogo.add(panelBienvenida, "INICIO");
        panelogo.add(panelPedido, "PEDIDO");
        panelogo.add(panelEncargado, "ENCARGADO");

        contentPane.add(panelogo, BorderLayout.CENTER);

        mostrarInicio();
    }

    private void resaltarBotonActivo(BotonFlat btnActivo) {
        btnInicio.setActivo(false);
        btnpedido.setActivo(false);
        btnEncargado.setActivo(false);
        btnSalir.setActivo(false);
        if (btnActivo != null) {
            btnActivo.setActivo(true);
        }
    }

    // Pantalla de Inicio / Logo central
    private JPanel crearPanelBienvenida() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(COLOR_FONDO_CENTRAL);

        JPanel pnlCard = new JPanel(new GridLayout(6, 1, 0, 12));
        pnlCard.setBackground(new Color(245, 235, 220));
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BARRA_MENU, 3),
                BorderFactory.createEmptyBorder(32, 45, 32, 45)));

        // Dibujo vectorial limpio de una taza de café (sin emojis rotos)
        JPanel pnlIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                // Plato
                g2.setColor(COLOR_BARRA_MENU);
                g2.fillOval(cx - 36, cy + 12, 72, 14);

                // Taza
                g2.fillRoundRect(cx - 24, cy - 14, 48, 30, 16, 16);

                // Asa de la taza
                g2.drawOval(cx + 18, cy - 8, 14, 18);

                // Vapor aromático
                g2.setColor(new Color(180, 130, 80));
                g2.drawArc(cx - 10, cy - 26, 10, 12, 0, 180);
                g2.drawArc(cx + 2, cy - 26, 10, 12, 0, 180);

                g2.dispose();
            }
        };
        pnlIcono.setOpaque(false);
        pnlIcono.setPreferredSize(new Dimension(80, 60));

        JLabel lblNom = new JLabel("CAFETERÍA AURORA", SwingConstants.CENTER);
        lblNom.setFont(new Font("Georgia", Font.BOLD, 26));
        lblNom.setForeground(COLOR_TEXTO_TITULO);

        JLabel lblSub = new JLabel("Sistema de Ventas, Almacén y Control de Operaciones", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSub.setForeground(COLOR_BARRA_MENU);

        JLabel lblDesc = new JLabel("Seleccione una opción en el menú lateral para comenzar:", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(80, 50, 30));

        // Botones de acción rápida con estilo café flat y texto visible blanco
        JPanel pnlAccionesRapidas = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        pnlAccionesRapidas.setOpaque(false);

        BotonFlat btnIrPedido = new BotonFlat("Abrir Módulo de Pedidos", BotonFlat.Estilo.ACCION_CAFE);
        btnIrPedido.addActionListener(e -> mostrarPedido());

        BotonFlat btnIrAdmin = new BotonFlat("Panel del Encargado", BotonFlat.Estilo.ACCION_CAFE);
        btnIrAdmin.addActionListener(e -> mostrarEncargado());

        pnlAccionesRapidas.add(btnIrPedido);
        pnlAccionesRapidas.add(btnIrAdmin);

        pnlCard.add(pnlIcono);
        pnlCard.add(lblNom);
        pnlCard.add(lblSub);
        pnlCard.add(lblDesc);
        pnlCard.add(pnlAccionesRapidas);

        pnl.add(pnlCard);
        return pnl;
    }

    public void mostrarInicio() {
        cardLayout.show(panelogo, "INICIO");
        resaltarBotonActivo(btnInicio);
        lblEstadoSesion.setText("Modo: Inicio / Selección de Módulo");
    }

    // Pantalla del Trabajador / Barista
    public void mostrarPedido() {
        cardLayout.show(panelogo, "PEDIDO");
        resaltarBotonActivo(btnpedido);
        lblEstadoSesion.setText("Sesión Activa: Trabajador / Barista (Atención de Pedidos)");
        panelPedido.cargarProductos(0);
    }

    // Pantalla del Administrador / Encargado (con validación idéntica a C#)
    public void mostrarEncargado() {
        FrmLoginEncargado login = new FrmLoginEncargado(this);
        login.setVisible(true);

        if (!login.isAutenticado()) {
            return;
        }

        // Si la clave es correcta ("1234"), abre el panel del encargado
        cardLayout.show(panelogo, "ENCARGADO");
        resaltarBotonActivo(btnEncargado);
        lblEstadoSesion.setText("Sesión Activa: Administrador / Mariana Vega (Encargado)");
        panelEncargado.cargarResumenGeneral();
        panelEncargado.cargarComboBoxProductos();
        panelEncargado.cargarProductosMasVendidos();
    }

    private void confirmarSalida() {
        int r = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea salir del sistema?",
                "Confirmar Salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            try {
                gestor.persistirTodoEnArchivos();
            } catch (Exception ignored) {
            }
            dispose();
            System.exit(0);
        }
    }

    public GestorCafeteria getGestor() {
        return gestor;
    }
}
