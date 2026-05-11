package de.dhsn.todoapp.model;

import java.io.Serializable;

/**
 * Basisklasse für einen einzelnen Eintrag in einer Todo-Liste.
 * Kann erweitert werden, z.B. für Checkbox-Einträge.
 */
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;

    /**
     * Erstellt einen neuen TodoItem mit dem angegebenen Text.
     *
     * @param text der Inhalt des Eintrags
     */
    public TodoItem(String text) {
        this.text = text;
    }

    /**
     * Gibt den Text des Eintrags zurück.
     *
     * @return der Eintragstext
     */
    public String getText() {
        return text;
    }

    /**
     * Setzt den Text des Eintrags.
     *
     * @param text neuer Text
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
