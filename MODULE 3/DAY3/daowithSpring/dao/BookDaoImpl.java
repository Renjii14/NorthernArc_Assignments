package dao;

import entity.Book;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Locale.filter;
public class BookDaoImpl implements BookDao{

    Map<Integer,Book> map=new LinkedHashMap<>();

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
}
