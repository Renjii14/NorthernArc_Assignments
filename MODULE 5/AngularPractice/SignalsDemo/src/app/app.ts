import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ShowCount } from './features/count/components/show-count/show-count';
import { IncrementCount } from './features/count/components/increment-count/increment-count';
import { DecrementCount } from './features/count/components/decrement-count/decrement-count';
import { IncrementBy } from './features/count/components/increment-by/increment-by';
import { DecrementBy } from './features/count/components/decrement-by/decrement-by';
import { ShowName } from './features/names/components/show-name/show-name';
import { UpdateName } from './features/names/update-name/update-name';
import { AddName } from './features/names/components/add-name/add-name';
import { UpdateBook } from './features/books/components/update-book/update-book';
import { ShowBook } from './features/books/components/show-book/show-book';
import { AddBook } from './features/books/components/add-book/add-book';

@Component({
  selector: 'app-root',
  imports: [AddBook, ShowBook, UpdateBook],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
