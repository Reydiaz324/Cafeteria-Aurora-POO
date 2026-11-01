package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class BotonFlat extends JButton {
    private static final long serialVersionUID = 1L;

    public enum Estilo {
        MENU_LATERAL,
        ACCION_CAFE,
        CATEGORIA
    }

    private final Estilo estilo;
    private boolean hover = false;
    private boolean activo = false;

    // Colores exactos de C#
    public static final Color COLOR_MENU_NORMAL = new Color(76, 47, 29);
    public static final Color COLOR_MENU_HOVER = new Color(96, 57, 34);
    public static final Color COLOR_MENU_ACTIVO = new Color(74, 44, 29);
    public static final Color COLOR_INDICADOR_ACTIVO = new Color(225, 185, 130);

    public static final Color COLOR_CAFE_NORMAL = new Color(105, 64, 39);
    public static final Color COLOR_CAFE_HOVER = new Color(126, 77, 47);
    public static final Color COLOR_CAFE_PRESS = new Color(85, 48, 26);

    public BotonFlat(String texto, Estilo estilo) {
        super(texto);
        this.estilo = estilo;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (estilo == Estilo.MENU_LATERAL) {
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setHorizontalAlignment(JButton.LEFT);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 10));
        } else if (estilo == Estilo.CATEGORIA) {
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14));
        } else {
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 16, 7, 16));
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        repaint();
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (estilo == Estilo.MENU_LATERAL) {
            if (activo) {
                g2.setColor(COLOR_MENU_ACTIVO);
                g2.fillRect(0, 0, w, h);
                // Línea indicadora dorada en el borde izquierdo
                g2.setColor(COLOR_INDICADOR_ACTIVO);
                g2.fillRect(0, 0, 5, h);
            } else if (hover) {
                g2.setColor(COLOR_MENU_HOVER);
                g2.fillRect(0, 0, w, h);
            } else {
                g2.setColor(COLOR_MENU_NORMAL);
                g2.fillRect(0, 0, w, h);
            }
        } else if (estilo == Estilo.CATEGORIA) {
            g2.setColor(hover ? COLOR_CAFE_HOVER : COLOR_CAFE_NORMAL);
            g2.fillRoundRect(0, 0, w, h, 6, 6);
            if (activo) {
                g2.setColor(COLOR_INDICADOR_ACTIVO);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 6, 6);
            }
        } else {
            // ACCION_CAFE
            g2.setColor(hover ? COLOR_CAFE_HOVER : COLOR_CAFE_NORMAL);
            g2.fillRoundRect(0, 0, w, h, 6, 6);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
