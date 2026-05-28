package de.dhsn.todoapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Todo-Liste mit Checkbox-Einträgen. Abgehakte Items werden ans Ende sortiert. */
public class CheckboxTodoList extends TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CheckboxTodoItem> items;

    public CheckboxTodoList(String title) {
        super(title);
        this.items = new ArrayList<>();
    }

    public void addItem(CheckboxTodoItem item) {
        items.add(item);
    }

    public void removeItem(CheckboxTodoItem item) {
        items.remove(item);
    }

    /**
     * Gibt alle Items zurück, unchecked zuerst und checked am Ende.
     *
     * @return sortierte Liste aller Einträge
     */
    public List<CheckboxTodoItem> getSortedItems() {
        List<CheckboxTodoItem> offen = new ArrayList<>();
        List<CheckboxTodoItem> erledigt = new ArrayList<>();
        for (CheckboxTodoItem item : items) {
            if (item.isChecked()) {
                erledigt.add(item);
            } else {
                offen.add(item);
            }
        }
        List<CheckboxTodoItem> result = new ArrayList<>(offen);
        result.addAll(erledigt);
        return result;
    }

    public List<CheckboxTodoItem> getItems() {
        return items;
    }
}
