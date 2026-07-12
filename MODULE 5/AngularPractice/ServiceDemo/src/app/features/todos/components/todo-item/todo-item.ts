import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { TodoDTO } from '../../../../dto/TodoDTO';
import { TodoService } from '../../services/todo-service';

@Component({
  selector: 'app-todo-item',
  imports: [],
  templateUrl: './todo-item.html',
  styleUrl: './todo-item.css',
})
export class TodoItem {
 
  @Input()
  public todo!: TodoDTO;

  todoService :TodoService= inject(TodoService);

}
