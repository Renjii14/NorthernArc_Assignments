package dao;

import Connection.DBmanager;
import entity.Book;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BookDaoJdbcImpl implements BookDao {

    private final DBmanager dBmanager;

    public BookDaoJdbcImpl(DBmanager dBmanager) {
        this.dBmanager = dBmanager;
    }

    public Book mapToBook(ResultSet rs) throws SQLException {

        return new Book(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
        );
    }

    @Override
    public void save(Book book) {

        try {
            Connection con = dBmanager.getConnection();

            String sql =
                    "insert into book values(?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, book.getId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getPublisher());

            int row = ps.executeUpdate();

            con.close();

            System.out.println("Rows Added : " + row);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book findbyId(int id) {

        try {
            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book where id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return mapToBook(rs);
            }

            con.close();

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateByid(int id, Book book) {

        try {
            Connection con = dBmanager.getConnection();

            String sql =
                    "update book set title=?,author=?,publisher=? where id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setInt(4, id);

            int row = ps.executeUpdate();

            con.close();

            System.out.println("Rows Updated : " + row);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(int id) {

        try {
            Connection con = dBmanager.getConnection();

            String sql =
                    "delete from book where id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            int row = ps.executeUpdate();

            con.close();

            System.out.println("Rows Deleted : " + row);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {

        try {
            Connection con = dBmanager.getConnection();

            String sql = "delete from book";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            int row = ps.executeUpdate();

            con.close();

            System.out.println("Rows Deleted : " + row);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> findAll() {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql = "select * from book";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> findByAuthor(String author) {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book where author=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, author);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> findByTitle(String title) {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book where title=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, title);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> sortByTitleAsc() {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book order by title asc";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> sortByTitleDesc() {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book order by title desc";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> findByAuthorandPublisher(String author,
                                                     String publisher) {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book where author=? and publisher=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, author);
            ps.setString(2, publisher);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Book> findByAuthorandtitle(String author,
                                                 String title) {

        try {
            List<Book> books = new LinkedList<>();

            Connection con = dBmanager.getConnection();

            String sql =
                    "select * from book where author=? and title=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, author);
            ps.setString(2, title);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

            con.close();

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}