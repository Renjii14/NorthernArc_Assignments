package com.northernArc.firstbootapp.dao;


import com.northernArc.firstbootapp.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class BookDaoImpl implements BookDao{

    Map<Integer, Book> map;

    @Override
    public void save(Book book) {
        map.put(book.getId(),book);

    }

    @Override
    public Book findbyId(int id) {
        return map.get(id);
    }

    @Override
    public void deleteById(int id) {
        map.remove(id);
    }

    @Override
    public void updateByid(int id, Book book) {
        map.put(id,book);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public Collection<Book> findAll() {
        return map.values();
    }

    @Override
    public Collection<Book> findByAuthor(String author) {
        return map.values()
                .stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> findByTitle(String title) {
        return map.values()
                .stream()
                .filter(book->book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> sortByTitleAsc() {
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> sortByTitleDesc() {
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> findByAuthorandPublisher(String author,String publisher) {
        return map.values()
                .stream()
                .filter(book ->
                        book.getAuthor().equalsIgnoreCase(author) &&
                                book.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> findByAuthorandtitle(String author,String title) {
        return map.values()
                .stream()
                .filter(book ->
                        book.getAuthor().equalsIgnoreCase(author) &&
                                book.getPublisher().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }


    @PostConstruct
    public void init(){
        map=new HashMap<>();
        map.put(101,
                new Book(101,"Java Complete Reference",
                        "Herbert Schildt","McGraw Hill"));

        map.put(102,
                new Book(102,"Effective Java",
                        "Joshua Bloch","Pearson"));

        map.put(103,
                new Book(103,"Head First Java",
                        "Kathy Sierra","O'Reilly"));
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroying...");
        map.clear();
    }
}
