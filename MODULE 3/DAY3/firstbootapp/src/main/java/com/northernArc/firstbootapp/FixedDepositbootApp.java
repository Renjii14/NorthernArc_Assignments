package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.controller.FixedDepositDaoConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FixedDepositbootApp implements CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication.run(FixedDepositbootApp.class, args);
    }


    @Autowired
    private FixedDepositDaoConsoleController fixedDepositDaoConsoleController;

    @Override
    public void run(String... args) throws Exception {
        fixedDepositDaoConsoleController.showMenu();
    }
}