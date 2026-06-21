package com.northernarc.restapidemo.dao;

import com.northernarc.restapidemo.model.Book;

import java.util.Collection;

public interface BookDao {
    public Book save(Book book);
    public Book findbyId(int id);
    public void deleteById(int id);
    public Book updateByid(int id, Book book);
    public void deleteAll();
    public Collection<Book> findAll();


}
