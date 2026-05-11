package de.dhsn.todoapp.model;

import java.io.Serializable;

/**
 * Erweiterung von TodoItem mit einem Checkbox-Status.
 * Abgehakte Einträge sollen im UI ans Ende sortiert werden.
 */
public class CheckboxTodoItem extends TodoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean checked;

    /**
     * Erstellt einen neuen Checkbox-Eintrag, standardmäßig nicht abgehakt.
     *
     * @param text der Eintragstext
     */
    public CheckboxTodoItem(String text) {
        super(text);
        this.checked = false;
    }

    /**
     * Gibt zurück ob der Eintrag abgehakt ist.
     *
     * @return true wenn abgehakt
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Setzt den Abgehakt-Status.
     *
     * @param checked neuer Status
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
