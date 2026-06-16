package main;

import config.SpringConfiguration;
import controller.TodoConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class TodoConsoleControllerMain {
    public static void main(String[] args) throws SQLException {
            ApplicationContext context= new AnnotationConfigApplicationContext(SpringConfiguration.class);
            TodoConsoleController todoConsoleController=context.getBean("todo",TodoConsoleController.class);
            todoConsoleController.printWelcomeMessage();
            todoConsoleController.showMenu();
        }
    }


