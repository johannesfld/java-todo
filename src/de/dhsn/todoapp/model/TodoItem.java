package de.dhsn.todoapp.model;

import java.io.Serializable;

/** Ein einzelner Eintrag in einer Todo-Liste. */
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;

    public TodoItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
