package entity;

public class Todo {

    private int todoId;
    private String task;
    private String status;

    public Todo() {
    }

    public Todo(int todoId,
                String task,
                String status) {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "Todo [todoId=" + todoId +
                ", task=" + task +
                ", status=" + status + "]";
    }
}