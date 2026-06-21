package com.northernarc.restapidemo.dao;

import com.northernarc.restapidemo.model.Book;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class BookDaoImpl implements BookDao{


    Map<Integer,Book>  map;

    @PostConstruct
    public void init(){
        map=new HashMap<>();
        map.put(1, new Book(1,
                "Java Complete Reference",
                "Herbert Schildt",
                "McGraw Hill"));

        map.put(2, new Book(2,
                "Effective Java",
                "Joshua Bloch",
                "Addison-Wesley"));

        map.put(3, new Book(3,
                "Clean Code",
                "Robert C. Martin",
                "Prentice Hall"));

    }


    @Override
    public Book save(Book book) {
        return map.put(book.getId(),book);

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
    public Book updateByid(int id, Book book) {
        return map.put(id,book);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public Collection<Book> findAll() {
        return map.values();
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroying mapping...");
        map.clear();
    }
}
