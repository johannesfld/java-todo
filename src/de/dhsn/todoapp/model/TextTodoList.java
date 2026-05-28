package de.dhsn.todoapp.model;

import java.io.Serializable;

/** Todo-Liste als freier Fließtext. */
public class TextTodoList extends TodoList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    public TextTodoList(String title) {
        super(title);
        this.content = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
