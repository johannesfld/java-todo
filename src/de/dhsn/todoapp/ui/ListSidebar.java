package de.dhsn.todoapp.ui;

import de.dhsn.todoapp.model.CheckboxTodoList;
import de.dhsn.todoapp.model.TodoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Linke Sidebar mit der Übersicht aller Todo-Listen.
 * Enthält einen Button zum Erstellen neuer Listen.
 */
public class ListSidebar extends JPanel {

    private DefaultListModel<TodoList> listModel;
    private JList<TodoList> jList;
    private Consumer<TodoList> onSelect;
    private Runnable onNew;
    private Consumer<TodoList> onDelete;

    /**
     * Erstellt die Sidebar.
     *
     * @param onSelect Callback wenn eine Liste ausgewählt wird
     * @param onNew    Callback wenn "Neue Liste" geklickt wird
     * @param onDelete Callback wenn eine Liste gelöscht werden soll
     */
    public ListSidebar(Consumer<TodoList> onSelect, Runnable onNew, Consumer<TodoList> onDelete) {
        this.onSelect = onSelect;
        this.onNew = onNew;
        this.onDelete = onDelete;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(210, 0));
        setBackground(Theme.BG_SIDEBAR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.SIDEBAR_BORDER));
        buildUI();
    }

    private void buildUI() {
        listModel = new DefaultListModel<>();
        jList = new JList<>(listModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setBackground(Theme.BG_SIDEBAR);
        jList.setOpaque(true);
        jList.setFixedCellHeight(Theme.ROW_HEIGHT);
        jList.setCellRenderer(new SidebarCellRenderer());
        jList.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        jList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jList.getSelectedValue() != null) {
                onSelect.accept(jList.getSelectedValue());
            }
        });

        // rechtsklick zeigt kontextmenü mit löschen
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            private void showPopup(MouseEvent e) {
                int idx = jList.locationToIndex(e.getPoint());
                if (idx < 0) return;
                jList.setSelectedIndex(idx);
                TodoList list = listModel.get(idx);
                JPopupMenu menu = new JPopupMenu();
                JMenuItem del = new JMenuItem("Liste löschen");
                del.addActionListener(ae -> onDelete.accept(list));
                menu.add(del);
                menu.show(jList, e.getX(), e.getY());
            }
        });

        JLabel title = new JLabel("Meine Listen");
        title.setFont(Theme.FONT_SIDEBAR_TITLE);
        title.setForeground(Theme.TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(14, Theme.PADDING_SIDEBAR_H, 10, 8));

        JScrollPane scroll = new JScrollPane(jList);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(Theme.BG_SIDEBAR);

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        RoundedButton newBtn = new RoundedButton("+ Neue Liste", true);
        newBtn.setFocusable(false);
        newBtn.addActionListener(e -> onNew.run());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Theme.BG_SIDEBAR);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));
        bottom.add(newBtn, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Befüllt die Liste mit den übergebenen Listen.
     *
     * @param lists alle vorhandenen TodoLists
     */
    public void setLists(List<TodoList> lists) {
        listModel.clear();
        for (TodoList l : lists) {
            listModel.addElement(l);
        }
    }

    /**
     * Fügt eine neue Liste ans Ende der Sidebar ein.
     *
     * @param list die hinzuzufügende Liste
     */
    public void addList(TodoList list) {
        listModel.addElement(list);
        jList.setSelectedValue(list, true);
    }

    /**
     * Gibt die aktuell ausgewählte Liste zurück.
     *
     * @return ausgewählte TodoList oder null
     */
    public TodoList getSelectedList() {
        return jList.getSelectedValue();
    }

    /**
     * Custom Renderer für die JList. Gerundetes Highlight bei Selektion plus Typ-Badge.
     */
    private static class SidebarCellRenderer extends JPanel
            implements ListCellRenderer<TodoList> {

        private final JLabel nameLabel = new JLabel();
        private final JLabel badge     = new JLabel();
        private boolean selected;

        SidebarCellRenderer() {
            setLayout(new BorderLayout(8, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(
                Theme.PADDING_SIDEBAR_V, Theme.PADDING_SIDEBAR_H,
                Theme.PADDING_SIDEBAR_V, 8));

            nameLabel.setFont(Theme.FONT_LIST_ITEM);

            badge.setFont(Theme.FONT_BADGE);
            badge.setOpaque(true);
            badge.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

            add(nameLabel, BorderLayout.CENTER);
            add(badge,     BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends TodoList> list, TodoList value,
                int index, boolean isSelected, boolean cellHasFocus) {

            this.selected = isSelected;
            nameLabel.setText(value.getTitle());
            nameLabel.setForeground(isSelected ? Theme.ACCENT : Theme.TEXT_DARK);

            boolean isCheckbox = value instanceof CheckboxTodoList;
            badge.setText(isCheckbox ? "✓" : "T");
            badge.setBackground(isCheckbox ? Theme.BADGE_CHECKBOX_BG : Theme.BADGE_TEXT_BG);
            badge.setForeground(isCheckbox ? Theme.BADGE_CHECKBOX_FG : Theme.BADGE_TEXT_FG);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (selected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT_LIGHT);
                // leicht eingerückt damit der rand der sidebar sichtbar bleibt
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4,
                                 Theme.ARC_SELECTION, Theme.ARC_SELECTION);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}
