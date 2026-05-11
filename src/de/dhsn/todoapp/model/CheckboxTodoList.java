package de.dhsn.todoapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Todo-Liste mit Checkbox-Einträgen.
 * Abgehakte Items werden beim Abrufen ans Ende sortiert.
 */
public class CheckboxTodoList extends TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CheckboxTodoItem> items;

    /**
     * Erstellt eine neue Checkbox-Liste ohne Einträge.
     *
     * @param title der Listenname
     */
    public CheckboxTodoList(String title) {
        super(title);
        this.items = new ArrayList<>();
    }

    /**
     * Fügt einen neuen Eintrag hinzu.
     *
     * @param item der neue Checkbox-Eintrag
     */
    public void addItem(CheckboxTodoItem item) {
        items.add(item);
    }

    /**
     * Entfernt einen Eintrag aus der Liste.
     *
     * @param item der zu entfernende Eintrag
     */
    public void removeItem(CheckboxTodoItem item) {
        items.remove(item);
    }

    /**
     * Gibt alle Items zurück – unchecked zuerst, checked ans Ende sortiert.
     *
     * @return sortierte Liste aller Einträge
     */
    public List<CheckboxTodoItem> getSortedItems() {
        // sort checked items to bottom
        List<CheckboxTodoItem> unchecked = new ArrayList<>();
        List<CheckboxTodoItem> checked = new ArrayList<>();
        for (CheckboxTodoItem item : items) {
            if (item.isChecked()) {
                checked.add(item);
            } else {
                unchecked.add(item);
            }
        }
        List<CheckboxTodoItem> result = new ArrayList<>(unchecked);
        result.addAll(checked);
        return result;
    }

    /**
     * Gibt die rohe, unsortierte Liste zurück (für Persistenz).
     *
     * @return interne Item-Liste
     */
    public List<CheckboxTodoItem> getItems() {
        return items;
    }
}
