package de.dhsn.todoapp.model;

import java.io.Serializable;

/**
 * Abstrakte Basisklasse für alle Todo-Listen.
 * Enthält Titel und gemeinsame Grundfunktionalität.
 */
public abstract class TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    /**
     * Erstellt eine neue Liste mit dem angegebenen Titel.
     *
     * @param title der Listenname
     */
    public TodoList(String title) {
        this.title = title;
    }

    /**
     * Gibt den Titel der Liste zurück.
     *
     * @return Listenname
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel der Liste.
     *
     * @param title neuer Name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    // für die Sidebar-Anzeige
    @Override
    public String toString() {
        return title;
    }
}
