package main;

import config.SpringConfiguration;
import controller.FixedDepositConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class FixedDepositMain {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FixedDepositConsoleController fixedDepositConsoleController = context.getBean("fdcontroller", FixedDepositConsoleController.class);
        fixedDepositConsoleController.welcome();
        fixedDepositConsoleController.showMenu();
    }
}