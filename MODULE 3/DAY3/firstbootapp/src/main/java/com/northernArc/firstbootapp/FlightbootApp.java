package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.controller.FlightDaoConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightbootApp implements CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication.run(FlightbootApp.class, args);
    }


    @Autowired
   private FlightDaoConsoleController flightDaoConsoleController;

    @Override
    public void run(String... args) throws Exception {
        flightDaoConsoleController.menu();
    }
}
