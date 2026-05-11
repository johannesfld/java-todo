package de.dhsn.todoapp.ui;

import de.dhsn.todoapp.model.TextTodoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Panel zur Anzeige und Bearbeitung einer Fließtext-Liste.
 * Der Text wird direkt in einer JTextArea bearbeitet.
 */
public class TextListPanel extends JPanel {

    private TextTodoList list;
    private JTextArea textArea;

    /**
     * Erstellt das Panel und bindet die Liste.
     *
     * @param list die anzuzeigende TextTodoList
     */
    public TextListPanel(TextTodoList list) {
        this.list = list;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_CONTENT);
        buildUI();
    }

    private void buildUI() {
        // header mit titel und typ-badge
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setBackground(Theme.BG_CONTENT);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel titleLbl = new JLabel(list.getTitle(), SwingConstants.LEFT);
        titleLbl.setFont(Theme.FONT_HEADER);
        titleLbl.setForeground(Theme.TEXT_DARK);

        JLabel badge = new JLabel("Fließtext");
        badge.setFont(Theme.FONT_BADGE);
        badge.setForeground(Theme.BADGE_TEXT_FG);
        badge.setBackground(Theme.BADGE_TEXT_BG);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        headerPanel.add(titleLbl, BorderLayout.WEST);
        headerPanel.add(badge,    BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        textArea = new JTextArea(list.getContent());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(Theme.FONT_BASE);
        textArea.setForeground(Theme.TEXT_DARK);
        textArea.setBackground(Theme.BG_CONTENT);
        textArea.setCaretColor(Theme.ACCENT);
        textArea.setSelectionColor(Theme.ACCENT_LIGHT);
        textArea.setMargin(new Insets(12, 16, 12, 16));

        // beim verlieren des fokus speichern wir den text zurück ins modell
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                list.setContent(textArea.getText());
            }
        });

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.DIVIDER));
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Schreibt den aktuellen Textinhalt ins Modell zurück.
     * Wird vor dem Speichern aufgerufen.
     */
    public void flushToModel() {
        list.setContent(textArea.getText());
    }
}
