package com.northernArc.firstbootapp.controller;
import com.northernArc.firstbootapp.dao.BookDao;
import com.northernArc.firstbootapp.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;
@Service
public class BookDaoConsoleController {
    @Autowired
    private Scanner scanner;
    @Autowired
    private BookDao bookDao;

    public void welcome(){
        System.out.println("This is Book console controller");
    }
    public void showMenu() {
        int choice;
        do {
            System.out.println("\n===== BOOK MANAGEMENT =====");
            System.out.println("1. Add Book");
            System.out.println("2. Find Book By Id");
            System.out.println("3. View All Books");
            System.out.println("4. Delete Book By Id");
            System.out.println("5. Update Book");
            System.out.println("6. Find By Author");
            System.out.println("7. Find By Title");
            System.out.println("8. Sort By Title Asc");
            System.out.println("9. Sort By Title Desc");
            System.out.println("10. Find By Author And Publisher");
            System.out.println("11. Find By Author And Title");
            System.out.println("12. Delete All Books");
            System.out.println("0. Exit");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> save();
                case 2 -> findById();
                case 3 -> findAll();
                case 4 -> deleteById();
                case 5 -> update();
                case 6 -> findByAuthor();
                case 7 -> findByTitle();
                case 8 -> sortByTitleAsc();
                case 9 -> sortByTitleDesc();
                case 10 -> findByAuthorAndPublisher();
                case 11 -> findByAuthorAndTitle();
                case 12 -> deleteAll();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid Choice");
            }

        } while (choice != 0);


    }

    private void save() {

        System.out.println("Enter Book Id:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Title:");
        String title = scanner.nextLine();

        System.out.println("Enter Author:");
        String author = scanner.nextLine();

        System.out.println("Enter Publisher:");
        String publisher = scanner.nextLine();

        Book book = new Book(id, title, author, publisher);

        bookDao.save(book);

        System.out.println("Book Added Successfully");
    }

    private void findById() {

        System.out.println("Enter Book Id:");
        int id = scanner.nextInt();

        Book book = bookDao.findbyId(id);

        if (book != null)
            System.out.println(book);
        else
            System.out.println("Book Not Found");
    }

    private void findAll() {

        java.util.Collection<Book> books = bookDao.findAll();

        books.forEach(System.out::println);
    }

    private void deleteById() {

        System.out.println("Enter Book Id:");
        int id = scanner.nextInt();

        bookDao.deleteById(id);

        System.out.println("Book Deleted");
    }

    private void update() {

        System.out.println("Enter Book Id To Update:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter New Title:");
        String title = scanner.nextLine();

        System.out.println("Enter New Author:");
        String author = scanner.nextLine();

        System.out.println("Enter New Publisher:");
        String publisher = scanner.nextLine();

        Book book = new Book(id, title, author, publisher);

        bookDao.updateByid(id, book);

        System.out.println("Book Updated");
    }

    private void findByAuthor() {

        System.out.println("Enter Author:");
        String author = scanner.nextLine();

        bookDao.findByAuthor(author)
                .forEach(System.out::println);
    }

    private void findByTitle() {

        System.out.println("Enter Title:");
        String title = scanner.nextLine();

        bookDao.findByTitle(title)
                .forEach(System.out::println);
    }

    private void sortByTitleAsc() {

        bookDao.sortByTitleAsc()
                .forEach(System.out::println);
    }

    private void sortByTitleDesc() {

        bookDao.sortByTitleDesc()
                .forEach(System.out::println);
    }

    private void findByAuthorAndPublisher() {

        System.out.println("Enter Author:");
        String author = scanner.nextLine();

        System.out.println("Enter Publisher:");
        String publisher = scanner.nextLine();

        bookDao.findByAuthorandPublisher(author, publisher)
                .forEach(System.out::println);
    }

    private void findByAuthorAndTitle() {

        System.out.println("Enter Author:");
        String author = scanner.nextLine();

        System.out.println("Enter Title:");
        String title = scanner.nextLine();

        bookDao.findByAuthorandtitle(author, title)
                .forEach(System.out::println);
    }

    private void deleteAll() {

        bookDao.deleteAll();

        System.out.println("All Books Deleted");
    }
}
