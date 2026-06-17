package main;

import config.SpringConfiguration;
import controller.FlightDaoConsoleController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FlightDaoMain {
    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FlightDaoConsoleController flightDaoConsoleController=context.getBean("a",FlightDaoConsoleController.class);
        flightDaoConsoleController.menu();
    }
}
