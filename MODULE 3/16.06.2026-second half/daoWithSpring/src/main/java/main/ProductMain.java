package main;

import config.SpringConfiguration;
import controller.ProductConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class ProductMain {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfiguration.class);
        ProductConsoleController productConsoleController=context.getBean("prodcontroller",ProductConsoleController.class);
        productConsoleController.welcome();
        productConsoleController.showMenu();

    }
}
