package org.northernarc.librarymanagement.controller;

import jakarta.validation.Valid;
import org.northernarc.librarymanagement.dto.BookRequestDTO;
import org.northernarc.librarymanagement.dto.BookResponseDTO;
import org.northernarc.librarymanagement.dto.BookUpdateDTO;
import org.northernarc.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {

        return ResponseEntity.ok(
                bookService.addBook(bookRequestDTO));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {

        return ResponseEntity.ok(
                bookService.updateBook(
                        bookId,
                        bookUpdateDTO));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @PathVariable Long bookId) {

        return ResponseEntity.ok(
                bookService.getBookById(bookId));
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {

        return ResponseEntity.ok(
                bookService.getAllBook());
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Long bookId) {

        bookService.deleteBook(bookId);

        return ResponseEntity.ok(
                "Book deleted successfully");
    }
}