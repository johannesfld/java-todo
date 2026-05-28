package de.dhsn.todoapp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Button mit abgerundeten Ecken, gemalt ohne externe Libraries.
 * primary=true: Akzent-Hintergrund + weißer Text; false: outline-stil.
 */
public class RoundedButton extends JButton {

    private final boolean primary;
    private boolean hovered = false;

    /**
     * Erstellt einen neuen RoundedButton.
     *
     * @param text    Beschriftung
     * @param primary true für Akzent-Stil, false für Outline-Stil
     */
    public RoundedButton(String text, boolean primary) {
        super(text);
        this.primary = primary;
        // wir malen selbst, standard-rendering aus
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(Theme.FONT_BASE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight(), arc = Theme.ARC_BUTTON;

        if (primary) {
            g2.setColor(hovered ? Theme.ACCENT.darker() : Theme.ACCENT);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(Color.WHITE);
        } else {
            g2.setColor(hovered ? Theme.HOVER_ROW : Theme.BG_CONTENT);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(Theme.DIVIDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
            g2.setColor(Theme.TEXT_DARK);
        }

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        String txt = getText();
        int tx = (w - fm.stringWidth(txt)) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(txt, tx, ty);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.max(d.height, 34);
        return d;
    }
}
