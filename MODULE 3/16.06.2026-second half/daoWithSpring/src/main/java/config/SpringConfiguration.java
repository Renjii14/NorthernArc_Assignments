package config;

import Connection.DBmanager;
import controller.FixedDepositConsoleController;
import controller.ProductConsoleController;
import controller.TodoConsoleController;
import dao.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Scanner;

@Configuration
public class SpringConfiguration {

    //todo

    @Bean("todo")
    public TodoDao todoDao() {
        return new TodoDaoImplCollection();
    }

    @Bean("todojdbc")
    @Primary
    public TodoDao todoDao2(DBmanager dbManager){
        return new TodoDaoImplJdbc(dbManager);
    }

    @Bean
    public TodoConsoleController consoleController(Scanner scanner,TodoDao todoDao){
        return new TodoConsoleController(scanner,todoDao);
    }



    //product
    @Bean("product")
    public ProductDao productDao(){
        return new ProductDaoImplCollections();
    }

    @Bean("productjdbc")
    public ProductDao productDao2(DBmanager dbmanager){
        return new ProductDaoJdbcImpl(dbmanager);
    }
    @Bean("prodcontroller")
    public ProductConsoleController productConsoleController(Scanner scanner,@Qualifier("product") ProductDao productDao){
        return new ProductConsoleController(scanner,productDao);
    }
    @Bean("prodcontrollerjdbc")
    public ProductConsoleController productConsoleController2(Scanner scanner,@Qualifier("productjdbc") ProductDao productDao){
        return new ProductConsoleController(scanner,productDao);
    }


    // Fixed Deposit

    @Bean("fixeddeposit")
    public FixedDepositDao fixedDepositDao() {
        return new FixedDepositDaoImplCollections();
    }

    @Bean("fixeddepositjdbc")
    public FixedDepositDao fixedDepositDaoJdbc(DBmanager dbmanager) {
        return new FixedDepositDaoImplJdbc(dbmanager);
    }

    @Bean("fdcontroller")
    public FixedDepositConsoleController fixedDepositConsoleController(
            Scanner scanner,
            @Qualifier("fixeddeposit") FixedDepositDao fixedDepositDao) {

        return new FixedDepositConsoleController(
                scanner,
                fixedDepositDao);
    }

    @Bean("fdcontrollerjdbc")
    public FixedDepositConsoleController fixedDepositConsoleControllerJdbc(
            Scanner scanner,
            @Qualifier("fixeddepositjdbc") FixedDepositDao fixedDepositDao) {

        return new FixedDepositConsoleController(
                scanner,
                fixedDepositDao);
    }








    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }


    @Bean
    public DBmanager getDbManager(){
        return new DBmanager(
                "jdbc:postgresql://localhost:5432/northernarc",
                "postgres",
                "12345");
    }
}