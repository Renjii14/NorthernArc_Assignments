import { Component, signal } from '@angular/core';
import { ShowEmployees } from './components/show-employees/show-employees';

import { AddEmployee } from './components/add-employee/add-employee';
import { UpdateEmployee } from './components/update-employee/update-employee';

@Component({
  selector: 'app-root',
  imports: [ShowEmployees, AddEmployee, UpdateEmployee],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ApiIntegration');
}
