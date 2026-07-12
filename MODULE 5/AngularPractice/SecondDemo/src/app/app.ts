import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { People } from './features/people/components/people/people';
import { Person } from './features/people/components/person/person';

@Component({
  selector: 'app-root',
  imports: [People],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('SecondDemo');
}
