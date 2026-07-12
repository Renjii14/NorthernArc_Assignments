import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book-service';
import { BookDTO } from '../../dto/BookDTO';

@Component({
  selector: 'app-update-book',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-book.html',
  styleUrl: './update-book.css',
})
export class UpdateBook {

  bookService = inject(BookService);

  id: number = 0;

  book: BookDTO = {
    id: 0,
    title: '',
    author: '',
    publisher: ''
  };

  updateBook() {

    if (
      this.id <= 0 ||
      !this.book.title.trim() ||
      !this.book.author.trim() ||
      !this.book.publisher.trim()
    ) {
      alert('Please fill all fields.');
      return;
    }

    this.bookService.updateBook(this.id, this.book);

    this.id = 0;

    this.book = {
      id: 0,
      title: '',
      author: '',
      publisher: ''
    };
  }

}