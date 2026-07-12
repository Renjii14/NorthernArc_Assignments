package config;

import connection.DBManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import todo.controller.TodoConsoleController;
import todo.dao.TodoDao;
import todo.daoImpl.TodoDaoImplJdbc;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "todo")
@PropertySource("classpath:application.properties")
public class TodoSpringConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DBManager dbManager(
            @Value("${db.url}") String url,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password,
            @Value("${db.driver}") String driver
    ) {
        return new DBManager(url, username, password, driver);
    }

    @Bean(name = "todojdbc")
    public TodoDao todoJdbcDao(DBManager dbManager) {
        return new TodoDaoImplJdbc(dbManager);
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public TodoConsoleController todoConsoleController(Scanner scanner, TodoDao todoDao) {
        return new TodoConsoleController(scanner, todoDao);
    }
}
