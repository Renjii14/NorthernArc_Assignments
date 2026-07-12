import { Injectable, signal, WritableSignal } from '@angular/core';
import { BookDTO } from '../dto/BookDTO';


@Injectable({
  providedIn: 'root'
})
export class BookService {

  private books: WritableSignal<BookDTO[]> = signal([
    { id: 1, title: 'Book 1', author: 'Author 1', publisher: 'Publisher 1' },
    { id: 2, title: 'Book 2', author: 'Author 2', publisher: 'Publisher 2' },
    { id: 3, title: 'Book 3', author: 'Author 3', publisher: 'Publisher 3' },
    { id: 4, title: 'Book 4', author: 'Author 4', publisher: 'Publisher 4' },
    { id: 5, title: 'Book 5', author: 'Author 5', publisher: 'Publisher 5' },
    { id: 6, title: 'Book 6', author: 'Author 6', publisher: 'Publisher 6' },
    { id: 7, title: 'Book 7', author: 'Author 7', publisher: 'Publisher 7' },
    { id: 8, title: 'Book 8', author: 'Author 8', publisher: 'Publisher 8' },
    { id: 9, title: 'Book 9', author: 'Author 9', publisher: 'Publisher 9' },
    { id: 10, title: 'Book 10', author: 'Author 10', publisher: 'Publisher 10' }
  ]);

  getBooks(): WritableSignal<BookDTO[]> {
    return this.books;
  }

  addBook(book: BookDTO): void {
    this.books.set([...this.books(), book]);
  }

  removeBook(id: number): void {
    this.books.set(
      this.books().filter(book => book.id !== id)
    );
  }

 updateBook(id: number, updatedBook: BookDTO): void {

  this.books.set(
    this.books().map(book =>
      book.id === id
        ? {
            ...updatedBook,
            id: id
          }
        : book
    )
  );

}

}