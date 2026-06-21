package org.northernarc.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todojdbc")

public class Todo {
    @Id
    @GeneratedValue
    private int todoId;
    private String task;
    private boolean status;

    public Todo() {
    }

    public Todo(int todoId, String task, boolean status) {
        this.todoId = todoId;
        this.task = task;
        this.status = status;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "Todo [todoId=" + todoId + ", task=" + task + ", status=" + status + "]";
    }
}
