package de.dhsn.todoapp.ui;

import de.dhsn.todoapp.model.CheckboxTodoItem;
import de.dhsn.todoapp.model.CheckboxTodoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel für Checkbox-Listen. Zeigt Items mit JCheckBox an,
 * abgehakte kommen ans Ende und werden durchgestrichen angezeigt.
 */
public class CheckboxListPanel extends JPanel {

    private CheckboxTodoList list;
    private JPanel itemsPanel;

    /**
     * Erstellt das Panel für die angegebene Checkbox-Liste.
     *
     * @param list die anzuzeigende CheckboxTodoList
     */
    public CheckboxListPanel(CheckboxTodoList list) {
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

        JLabel badge = new JLabel("Checkboxen");
        badge.setFont(Theme.FONT_BADGE);
        badge.setForeground(Theme.BADGE_CHECKBOX_FG);
        badge.setBackground(Theme.BADGE_CHECKBOX_BG);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        headerPanel.add(titleLbl, BorderLayout.WEST);
        headerPanel.add(badge,    BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Theme.BG_CONTENT);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.DIVIDER));
        scroll.getViewport().setBackground(Theme.BG_CONTENT);
        add(scroll, BorderLayout.CENTER);

        // button unten
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        bottom.setBackground(Theme.BG_CONTENT);
        RoundedButton addBtn = new RoundedButton("+ Eintrag hinzufügen", false);
        bottom.add(addBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String text = JOptionPane.showInputDialog(this, "Neuer Eintrag:");
            if (text != null && !text.trim().isEmpty()) {
                list.addItem(new CheckboxTodoItem(text.trim()));
                refresh();
            }
        });

        refresh();
    }

    /**
     * Baut die Item-Liste neu auf. Wird nach jeder Änderung aufgerufen.
     */
    public void refresh() {
        itemsPanel.removeAll();

        List<CheckboxTodoItem> sorted = list.getSortedItems();
        boolean separatorAdded = false;

        for (CheckboxTodoItem item : sorted) {
            if (item.isChecked() && !separatorAdded) {
                separatorAdded = true;
                JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                sep.setForeground(Theme.DIVIDER);
                sep.setBackground(Theme.BG_CONTENT);
                itemsPanel.add(sep);
            }
            itemsPanel.add(buildItemRow(item));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    /**
     * Erstellt eine Zeile für ein einzelnes Item mit Checkbox und Lösch-Button.
     *
     * @param item das darzustellende CheckboxTodoItem
     * @return fertiges JPanel für diese Zeile
     */
    private JPanel buildItemRow(CheckboxTodoItem item) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.ROW_HEIGHT));
        row.setPreferredSize(new Dimension(0, Theme.ROW_HEIGHT));
        row.setBackground(Theme.BG_CONTENT);
        row.setOpaque(true);
        row.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { row.setBackground(Theme.HOVER_ROW); }
            @Override public void mouseExited(MouseEvent e)  { row.setBackground(Theme.BG_CONTENT); }
        });

        JCheckBox cb = new JCheckBox();
        cb.setSelected(item.isChecked());
        cb.setOpaque(false);
        cb.setBackground(Theme.BG_CONTENT);

        JLabel label = new JLabel(item.getText());
        label.setFont(Theme.FONT_BASE);

        if (item.isChecked()) {
            label.setText("<html><strike>" + item.getText() + "</strike></html>");
            label.setForeground(Theme.TEXT_CHECKED);
        } else {
            label.setForeground(Theme.TEXT_DARK);
        }

        cb.addActionListener(e -> {
            item.setChecked(cb.isSelected());
            refresh();
        });

        RoundedButton del = new RoundedButton("✕", false);
        del.setFont(del.getFont().deriveFont(10f));
        del.setFocusable(false);
        del.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        del.addActionListener(e -> {
            list.removeItem(item);
            refresh();
        });

        row.add(cb,    BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        row.add(del,   BorderLayout.EAST);

        return row;
    }
}
