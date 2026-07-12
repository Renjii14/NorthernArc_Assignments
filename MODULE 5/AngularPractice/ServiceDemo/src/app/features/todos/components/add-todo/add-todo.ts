import { Component, inject } from '@angular/core';
import { TodoDTO } from '../../../../dto/TodoDTO';
import { TodoService } from '../../services/todo-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-todo',
  standalone: true,
  imports: [CommonModule,FormsModule
  ],
  templateUrl: './add-todo.html',
  styleUrl: './add-todo.css',
})
export class AddTodo {
  
  todoService :TodoService= inject(TodoService);
  newTodo:TodoDTO = {
    id: 0,
    title: '',
    description: '',
    completed: false
  };

  addTodo() {
      if (this.newTodo.title.trim() && this.newTodo.description.trim()&& this.newTodo.id) {
        this.todoService.addTodo({...this.newTodo});
        this.newTodo = {
          id: 0,
          title: '',
          description: '',
          completed: false
        };
      }
  } 
}
