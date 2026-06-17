package config;

import Connection.DBmanager;
import controller.BookDaoConsoleController;
import controller.TodoConsoleController;
import dao.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = {"config","controller","dao","entity","ui"})
public class BookConfig {

    @Bean("book")
    public BookDao bookDao() {
        return new BookDaoImpl();
    }
    @Bean("bookjdbc")
    public BookDao bookDao2(DBmanager dbManager){
        return new BookDaoJdbcImpl(dbManager);
    }
    @Bean("a")
    public BookDaoConsoleController consoleController(Scanner scanner, @Qualifier("book") BookDao bookDao){
        return new BookDaoConsoleController(scanner,bookDao);
    }
    @Bean("b")
    public BookDaoConsoleController consoleController2(Scanner scanner,@Qualifier("bookjdbc") BookDao bookDao){
        return new BookDaoConsoleController(scanner,bookDao);
    }

    @Bean
    public Scanner scanner(){
        return  new Scanner(System.in);
    }

    @Bean
    public DBmanager getDbManager(){
        return new DBmanager(
                "jdbc:postgresql://localhost:5432/northernarc",
                "postgres",
                "12345");
    }

}
