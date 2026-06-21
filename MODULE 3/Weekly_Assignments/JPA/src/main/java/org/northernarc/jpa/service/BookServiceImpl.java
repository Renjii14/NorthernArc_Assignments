package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Book;
import org.northernarc.jpa.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepo bookrepo;

    @Override
    public Book save(Book book) {
       return bookrepo.save(book);
    }

    @Override
    public Book findbyId(int id) {
        return bookrepo.findById(id).get();
    }



    @Override
    public void deleteById(int id) {
           bookrepo.deleteById(id);
    }

    @Override
    public Book updateByid(int id, Book book) {
        Book book1=bookrepo.findById(id).get();
        book1.setTitle(book.getTitle());
        book1.setAuthor(book.getAuthor());
        book1.setPublisher(book.getPublisher());
        bookrepo.save(book1);
        return  book1;
    }

    @Override
    public void deleteAll() {
     bookrepo.deleteAll();
    }

    @Override
    public Collection<Book> findAll() {
        return bookrepo.findAll();
    }
}
