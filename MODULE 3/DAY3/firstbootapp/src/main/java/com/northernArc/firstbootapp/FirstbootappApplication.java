package com.northernArc.firstbootapp;

import com.northernArc.firstbootapp.controller.TodoDaoConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstbootappApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FirstbootappApplication.class, args);
	}

	@Autowired
	private TodoDaoConsoleController todoDaoConsoleController;

	@Override
	public void run(String... args) throws Exception {
		todoDaoConsoleController.showMenu();
	}

}
