package main;

import config.SpringConfiguration;
import controller.ProductConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class ProductjdbcMain {
    public static void main(String[] args) throws SQLException {
        System.out.println("Product With Spring Config , JDBC");
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        ProductConsoleController productConsoleController = context.getBean("prodcontrollerjdbc", ProductConsoleController.class);
        productConsoleController.welcome();
        productConsoleController.showMenu();
    }
}