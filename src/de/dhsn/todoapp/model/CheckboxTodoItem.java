package de.dhsn.todoapp.model;

import java.io.Serializable;

/** Eintrag mit Checkbox-Status. Abgehakte Items wandern im UI ans Ende. */
public class CheckboxTodoItem extends TodoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean checked;

    public CheckboxTodoItem(String text) {
        super(text);
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
