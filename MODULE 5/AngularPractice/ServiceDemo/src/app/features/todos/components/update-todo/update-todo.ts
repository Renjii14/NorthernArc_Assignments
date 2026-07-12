import { Component, inject } from '@angular/core';
import { TodoService } from '../../services/todo-service';
import { FormsModule } from '@angular/forms';
import { TodoDTO } from '../../../../dto/TodoDTO';

@Component({
  selector: 'app-update-todo',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-todo.html',
  styleUrl: './update-todo.css',
})
export class UpdateTodo {

  todoService = inject(TodoService);

  updateTodoData: TodoDTO = {
    id: 0,
    title: '',
    description: '',
    completed: false
  };

  searchTodo() {

    const todo = this.todoService
      .getTodos()
      .find(t => t.id === this.updateTodoData.id);

    if (todo) {
      this.updateTodoData = { ...todo };
    } else {
      alert("Todo not found");
    }
  }

  updateTodo() {

    if (this.updateTodoData.id <= 0) {
      alert("Invalid Id");
      return;
    }

    if (this.updateTodoData.title.trim() === "") {
      alert("Title is required");
      return;
    }

    if (this.updateTodoData.description.trim() === "") {
      alert("Description is required");
      return;
    }

    this.todoService.updateTodo(this.updateTodoData);

    alert("Todo updated successfully!");

    this.updateTodoData = {
      id: 0,
      title: '',
      description: '',
      completed: false
    };
  }
}
