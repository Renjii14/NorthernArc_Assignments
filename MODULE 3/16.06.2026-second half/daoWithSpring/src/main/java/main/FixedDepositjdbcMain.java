package main;

import config.SpringConfiguration;
import controller.FixedDepositConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class FixedDepositjdbcMain {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FixedDepositConsoleController fixedDepositConsoleController = context.getBean("fdjdbccontroller", FixedDepositConsoleController.class);
        fixedDepositConsoleController.welcome();
        fixedDepositConsoleController.showMenu();
    }
}