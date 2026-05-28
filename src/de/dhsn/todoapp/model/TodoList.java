package de.dhsn.todoapp.model;

import java.io.Serializable;

/** Abstrakte Basisklasse für alle Todo-Listen. */
public abstract class TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    public TodoList(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // für die Sidebar-Anzeige
    @Override
    public String toString() {
        return title;
    }
}
