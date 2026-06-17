package main;

import config.BookConfig;
import controller.BookDaoConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BookDaoMain {
    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(BookConfig.class);
        BookDaoConsoleController bookDaoConsoleController=context.getBean("a",BookDaoConsoleController.class);
        bookDaoConsoleController.welcome();
        bookDaoConsoleController.showMenu();
    }
}
