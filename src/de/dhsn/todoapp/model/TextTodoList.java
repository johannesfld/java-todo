package de.dhsn.todoapp.model;

import java.io.Serializable;

/**
 * Todo-Liste die aus einem freien Fließtext besteht.
 * Der Text wird als einzelner String gespeichert.
 */
public class TextTodoList extends TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    /**
     * Erstellt eine neue Text-Liste mit leerem Inhalt.
     *
     * @param title der Listenname
     */
    public TextTodoList(String title) {
        super(title);
        this.content = "";
    }

    /**
     * Gibt den gespeicherten Fließtext zurück.
     *
     * @return der Text der Liste
     */
    public String getContent() {
        return content;
    }

    /**
     * Setzt den Fließtext der Liste.
     *
     * @param content neuer Inhalt
     */
    public void setContent(String content) {
        this.content = content;
    }
}
