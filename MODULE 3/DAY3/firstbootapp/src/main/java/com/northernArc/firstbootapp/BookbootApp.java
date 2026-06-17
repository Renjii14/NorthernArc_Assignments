package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.controller.BookDaoConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookbootApp implements CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication.run(FlightbootApp.class, args);
    }


    @Autowired
    private BookDaoConsoleController bookDaoConsoleController;

    @Override
    public void run(String... args) throws Exception {
        bookDaoConsoleController.showMenu();
    }
}