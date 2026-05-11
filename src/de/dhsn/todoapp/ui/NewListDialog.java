package de.dhsn.todoapp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Dialog zum Erstellen einer neuen Todo-Liste.
 * Fragt nach Titel und Typ (Text oder Checkbox) – typ-auswahl als klickbare karten.
 */
public class NewListDialog extends JDialog {

    private JTextField titleField;
    private boolean confirmed = false;
    private boolean checkboxSelected = false;

    /**
     * Erstellt den Dialog als modalen Dialog über dem Elternfenster.
     *
     * @param parent das übergeordnete Fenster
     */
    public NewListDialog(Frame parent) {
        super(parent, "Neue Liste erstellen", true);
        buildUI();
        setSize(360, 260);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG_CONTENT);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setContentPane(root);

        // titelfeld oben
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 6));
        top.setBackground(Theme.BG_CONTENT);
        top.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel titleLbl = new JLabel("Titel:");
        titleLbl.setFont(Theme.FONT_BASE);
        titleLbl.setForeground(Theme.TEXT_DARK);
        top.add(titleLbl);

        titleField = new JTextField();
        titleField.setFont(Theme.FONT_BASE);
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DIVIDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        top.add(titleField);
        root.add(top, BorderLayout.NORTH);

        // typ-karten in der mitte
        JPanel cardArea = new JPanel(new GridLayout(1, 2, 10, 0));
        cardArea.setBackground(Theme.BG_CONTENT);
        cardArea.setBorder(BorderFactory.createEmptyBorder(4, 16, 8, 16));

        JPanel textCard     = buildTypeCard("T",  "Fließtext",   "Freier Notiztext", false);
        JPanel checkboxCard = buildTypeCard("✓", "Checkboxen",  "Liste mit Haken",   true);
        cardArea.add(textCard);
        cardArea.add(checkboxCard);
        root.add(cardArea, BorderLayout.CENTER);

        // buttons unten
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.setBackground(Theme.BG_CONTENT);
        RoundedButton cancel = new RoundedButton("Abbrechen", false);
        RoundedButton ok     = new RoundedButton("Erstellen", true);
        buttons.add(cancel);
        buttons.add(ok);
        root.add(buttons, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte einen Titel eingeben.");
                return;
            }
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        // enter bestätigt
        getRootPane().setDefaultButton(ok);
    }

    /**
     * Erstellt eine klickbare Typ-Karte für die Listentyp-Auswahl.
     *
     * @param icon       symbol das angezeigt wird
     * @param label      name des typs
     * @param sub        kurze beschreibung
     * @param isCheckbox ob diese karte den checkbox-typ repräsentiert
     * @return fertiges karten-panel
     */
    private JPanel buildTypeCard(String icon, String label, String sub, boolean isCheckbox) {
        // anonymous panel das seinen eigenen border/hintergrund malt – status aus checkboxSelected
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = (isCheckbox == checkboxSelected);
                g2.setColor(sel ? Theme.ACCENT_LIGHT : Theme.BG_CONTENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                                 Theme.ARC_CARD, Theme.ARC_CARD);
                g2.setColor(sel ? Theme.ACCENT : Theme.DIVIDER);
                g2.setStroke(new BasicStroke(sel ? 2f : 1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2,
                                 Theme.ARC_CARD, Theme.ARC_CARD);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(130, 90));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        iconLbl.setForeground(Theme.ACCENT);
        card.add(iconLbl, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 2, 0);
        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLbl.setForeground(Theme.TEXT_DARK);
        card.add(nameLbl, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subLbl.setForeground(Theme.TEXT_SUB);
        card.add(subLbl, gbc);

        // klick wechselt checkboxSelected und beide karten werden neu gemalt
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkboxSelected = isCheckbox;
                // parent repainten damit beide karten ihren zustand aktualisieren
                card.getParent().repaint();
            }
        });

        return card;
    }

    /**
     * Gibt zurück ob der Nutzer auf "Erstellen" geklickt hat.
     *
     * @return true wenn bestätigt
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Gibt den eingegebenen Titel zurück.
     *
     * @return Titel-Text ohne führende/nachfolgende Leerzeichen
     */
    public String getListTitle() {
        return titleField.getText().trim();
    }

    /**
     * Gibt zurück ob der Nutzer "Checkboxen" ausgewählt hat.
     *
     * @return true für Checkbox-Liste, false für Text-Liste
     */
    public boolean isCheckboxType() {
        return checkboxSelected;
    }
}
