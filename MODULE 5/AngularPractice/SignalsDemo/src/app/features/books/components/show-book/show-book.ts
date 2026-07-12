import { Component, inject } from '@angular/core';
import { BookService } from '../../services/book-service';

@Component({
  selector: 'app-show-book',
  imports: [],
  templateUrl: './show-book.html',
  styleUrl: './show-book.css',
})
export class ShowBook {
  bookService = inject(BookService);
}
