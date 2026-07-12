import { Component, inject } from '@angular/core';
import { TodoDTO } from '../../../../dto/TodoDTO';
import { TodoService } from '../../services/todo-service';
import { TodoItem } from '../todo-item/todo-item';
import { AddTodo } from '../add-todo/add-todo';
import { FormsModule } from '@angular/forms';
import { UpdateTodo } from '../update-todo/update-todo';

@Component({
  selector: 'app-todos-component',
  standalone: true,
  imports: [FormsModule, TodoItem, AddTodo, UpdateTodo],
  templateUrl: './todos-component.html',
  styleUrl: './todos-component.css',
})
export class TodosComponent {
  todoService :TodoService= inject(TodoService);
}