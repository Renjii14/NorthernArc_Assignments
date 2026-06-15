package jdbcDao.dao;

import jdbcDao.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import Connection.DBmanager;

public class bookDaoImpl implements bookDao{
    @Override
    public int save(Book book) {
        return 0;
    }

    @Override
    public Book findbyId(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void updateByid(int id, Book book) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Collection<Book> findAll() {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DBmanager.getConnection()) {

            String sql = "SELECT * FROM book";
            PreparedStatement statement = conn.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Collection<Book> findByAuthor(String author) {
        return List.of();
    }

    @Override
    public Collection<Book> findByTitle(String title) {
        return List.of();
    }

    @Override
    public Collection<Book> sortByTitleAsc() {
        return List.of();
    }

    @Override
    public Collection<Book> sortByTitleDesc() {
        return List.of();
    }

    @Override
    public Collection<Book> findByAuthorandPublisher(String author) {
        return List.of();
    }

    @Override
    public Collection<Book> findByAuthorandtitle(String author) {
        return List.of();
    }
}
