package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.BookRequestDTO;
import org.northernarc.librarymanagement.dto.BookResponseDTO;
import org.northernarc.librarymanagement.dto.BookUpdateDTO;
import org.northernarc.librarymanagement.model.Book;
import org.northernarc.librarymanagement.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        Book book = mapToEntity(bookRequestDTO);
        Book savedbook=bookRepository.save(book);
        return mapToResponseDTO(savedbook);
    }

    @Override
    public BookResponseDTO updateBook(Long bookId, BookUpdateDTO bookUpdateDTO) {
        Book existingbook=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        existingbook.setTitle(bookUpdateDTO.getTitle());
        existingbook.setAuthor(bookUpdateDTO.getAuthor());
        existingbook.setIsbn(bookUpdateDTO.getIsbn());
        existingbook.setAvailableCopies(bookUpdateDTO.getAvailableCopies());
        Book updatedbook=bookRepository.save(existingbook);
        return mapToResponseDTO(updatedbook);
    }

    @Override
    public BookResponseDTO getBookById(Long bookId) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        return mapToResponseDTO(book);
    }

    @Override
    public List<BookResponseDTO> getAllBook() {
        List<Book> books=bookRepository.findAll();
        return books.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public void deleteBook(Long bookId) {
     Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
     bookRepository.delete(book);
    }

    private Book mapToEntity(BookRequestDTO bookRequestDTO){
        Book book = new Book();
        book.setTitle(bookRequestDTO.getTitle());
        book.setAuthor(bookRequestDTO.getAuthor());
        book.setIsbn(bookRequestDTO.getIsbn());
        book.setAvailableCopies(bookRequestDTO.getAvailableCopies());
        return book;
    }

    private BookResponseDTO mapToResponseDTO(Book book){
        BookResponseDTO bookResponseDTO=new BookResponseDTO();
        bookResponseDTO.setTitle(book.getTitle());
        bookResponseDTO.setAuthor(book.getAuthor());
        bookResponseDTO.setIsbn(book.getIsbn());
        bookResponseDTO.setAvailableCopies(book.getAvailableCopies());
        return bookResponseDTO;
    }
}
