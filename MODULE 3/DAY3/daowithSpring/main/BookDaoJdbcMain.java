package main;

import config.BookConfig;
import controller.BookDaoConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BookDaoJdbcMain {
    public static void main(String[] args) {
        System.out.println("Spring with jdbc");
        ApplicationContext context=new AnnotationConfigApplicationContext(BookConfig.class);
        BookDaoConsoleController bookDaoConsoleController=context.getBean("b",BookDaoConsoleController.class);
        bookDaoConsoleController.welcome();
        bookDaoConsoleController.showMenu();
    }
}
