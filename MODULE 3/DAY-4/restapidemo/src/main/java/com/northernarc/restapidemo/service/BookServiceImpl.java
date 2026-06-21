package com.northernarc.restapidemo.service;

import com.northernarc.restapidemo.dao.BookDao;
import com.northernarc.restapidemo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Override
    public Book save(Book book) {
       return bookDao.save(book);
    }

    @Override
    public Book findbyId(int id) {
        return bookDao.findbyId(id);
    }

    @Override
    public void deleteById(int id) {
           bookDao.deleteById(id);
    }

    @Override
    public Book updateByid(int id, Book book) {
        return bookDao.updateByid(id,book);
    }

    @Override
    public void deleteAll() {
     bookDao.deleteAll();
    }

    @Override
    public Collection<Book> findAll() {
        return bookDao.findAll();
    }
}
