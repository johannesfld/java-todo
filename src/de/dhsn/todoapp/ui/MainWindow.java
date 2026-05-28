package de.dhsn.todoapp.ui;

import de.dhsn.todoapp.model.CheckboxTodoList;
import de.dhsn.todoapp.model.TextTodoList;
import de.dhsn.todoapp.model.TodoList;
import de.dhsn.todoapp.persistence.PersistenceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Hauptfenster der Applikation. Enthält Sidebar und Content-Bereich.
 * Beim Schließen wird automatisch gespeichert.
 */
public class MainWindow extends JFrame {

    private List<TodoList> lists;
    private PersistenceManager persistence;
    private ListSidebar sidebar;
    private JPanel contentArea;
    // aktuell angezeigtes panel, brauchen wir zum flushen vor dem speichern
    private JPanel currentPanel;

    /**
     * Erstellt das Hauptfenster, lädt gespeicherte Daten und zeigt die UI an.
     *
     * @param persistence der PersistenceManager zum Laden/Speichern
     */
    public MainWindow(PersistenceManager persistence) {
        super("Todo App");
        this.persistence = persistence;
        this.lists = new ArrayList<>(persistence.load());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(860, 580);
        setMinimumSize(new Dimension(520, 380));
        setLocationRelativeTo(null);

        buildUI();
        setupWindowListener();

        // erste liste direkt auswählen wenn vorhanden
        if (!lists.isEmpty()) {
            sidebar.setLists(lists);
            showList(lists.get(0));
        } else {
            // sidebar trotzdem füllen, auch wenn leer (sonst zeigt sie alte Daten)
            sidebar.setLists(lists);
        }
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        sidebar = new ListSidebar(this::showList, this::createNewList, this::deleteList);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Theme.BG_CONTENT);
        contentArea.add(makePlaceholder(), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentArea);
        split.setDividerLocation(210);
        split.setDividerSize(1);
        split.setContinuousLayout(true);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel makePlaceholder() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_CONTENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel icon = new JLabel("☑");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 52));
        icon.setForeground(Theme.DIVIDER);
        p.add(icon, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(14, 0, 4, 0);
        JLabel title = new JLabel("Keine Liste ausgewählt");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Theme.TEXT_DARK);
        p.add(title, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel sub = new JLabel("Wähle links eine Liste aus oder erstelle eine neue.");
        sub.setFont(Theme.FONT_BASE);
        sub.setForeground(Theme.TEXT_SUB);
        p.add(sub, gbc);

        return p;
    }

    /**
     * Zeigt den Inhalt der angegebenen Liste im Content-Bereich an.
     *
     * @param list die anzuzeigende Liste
     */
    private void showList(TodoList list) {
        flushCurrentPanel();
        contentArea.removeAll();

        if (list instanceof CheckboxTodoList) {
            CheckboxListPanel panel = new CheckboxListPanel((CheckboxTodoList) list);
            currentPanel = panel;
            contentArea.add(panel, BorderLayout.CENTER);
        } else if (list instanceof TextTodoList) {
            TextListPanel panel = new TextListPanel((TextTodoList) list);
            currentPanel = panel;
            contentArea.add(panel, BorderLayout.CENTER);
        }

        contentArea.revalidate();
        contentArea.repaint();
    }

    /**
     * Öffnet den Dialog zum Erstellen einer neuen Liste.
     */
    private void createNewList() {
        NewListDialog dlg = new NewListDialog(this);
        dlg.setVisible(true);

        if (dlg.isConfirmed()) {
            TodoList newList;
            if (dlg.isCheckboxType()) {
                newList = new CheckboxTodoList(dlg.getListTitle());
            } else {
                newList = new TextTodoList(dlg.getListTitle());
            }
            lists.add(newList);
            sidebar.addList(newList);
            showList(newList);
        }
    }

    /**
     * Löscht die angegebene Liste nach Bestätigung durch den Nutzer.
     *
     * @param list die zu löschende Liste
     */
    private void deleteList(TodoList list) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Liste \"" + list.getTitle() + "\" wirklich löschen?",
                "Liste löschen",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            lists.remove(list);
            sidebar.setLists(lists);
            // content-bereich zurücksetzen
            currentPanel = null;
            contentArea.removeAll();
            contentArea.add(makePlaceholder(), BorderLayout.CENTER);
            contentArea.revalidate();
            contentArea.repaint();
        }
    }

    /**
     * Schreibt den Inhalt des aktuellen Panels ins Modell zurück.
     * Wichtig für TextListPanel wo der Text beim tippen noch nicht gespeichert ist.
     */
    private void flushCurrentPanel() {
        if (currentPanel instanceof TextListPanel) {
            ((TextListPanel) currentPanel).flushToModel();
        }
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                flushCurrentPanel();
                persistence.save(lists);
                dispose();
                System.exit(0);
            }
        });
    }
}
