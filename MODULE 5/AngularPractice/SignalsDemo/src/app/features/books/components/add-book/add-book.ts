import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../services/book-service';
import { BookDTO } from '../../dto/BookDTO';

@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-book.html',
  styleUrl: './add-book.css',
})
export class AddBook {

  bookService = inject(BookService);

  Book: BookDTO = {
    id: 0,
    title: '',
    author: '',
    publisher: ''
  };

  addBook() {

    if (
      !this.Book.title.trim() ||
      !this.Book.author.trim() ||
      !this.Book.publisher.trim()
    ) {
      alert('Please fill all fields');
      return;
    }

    this.bookService.addBook(this.Book);

    this.Book = {
      id: 0,
      title: '',
      author: '',
      publisher: ''
    };
  }

}