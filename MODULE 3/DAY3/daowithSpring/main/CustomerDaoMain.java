package main;

import config.BookConfig;
import config.CustomerConfig;
import controller.BookDaoConsoleController;
import controller.CustomerDaoConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CustomerDaoMain {

    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(CustomerConfig.class);
        CustomerDaoConsoleController customerDaoConsoleController=context.getBean(CustomerDaoConsoleController.class);
        customerDaoConsoleController.showMenu();
    }
}
