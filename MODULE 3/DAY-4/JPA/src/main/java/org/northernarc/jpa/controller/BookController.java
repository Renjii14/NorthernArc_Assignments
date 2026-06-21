package org.northernarc.jpa.controller;

import org.northernarc.jpa.model.Book;
import org.northernarc.jpa.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public Collection<Book> listAll(Book book){
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findbyId(@PathVariable int id){
        return new ResponseEntity<>(bookService.findbyId(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable int id){
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity deleteAll(){
        bookService.deleteAll();
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book){
        return ResponseEntity.status(201).body(bookService.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable int id,@RequestBody Book book){
         bookService.updateByid(id,book);
         return ResponseEntity.ok(book);
    }

}
