package ui;

import dao.TodoDao;
import dao.TodoDaoImpl;
import entity.Todo;

import java.sql.SQLException;
import java.util.Scanner;

public class TodoMain {

    private static Scanner sc =
            new Scanner(System.in);

    private static TodoDao todoDao =
            new TodoDaoImpl();

    public static void main(String[] args)
            throws SQLException {

        while(true) {

            System.out.println("\n===== TODO MENU =====");

            System.out.println("1. Add Todo");
            System.out.println("2. Find By Id");
            System.out.println("3. Find All Todos");
            System.out.println("4. Update Todo");
            System.out.println("5. Delete Todo");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1 -> addTodo();

                case 2 -> findById();

                case 3 -> findAll();

                case 4 -> updateTodo();

                case 5 -> deleteTodo();

                default -> {
                    System.out.println("Thank You");
                    return;
                }
            }
        }
    }

    static void addTodo()
            throws SQLException {

        System.out.print("Todo Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Task : ");
        String task = sc.nextLine();

        System.out.print("Status : ");
        String status = sc.nextLine();

        Todo todo =
                new Todo(id, task, status);

        System.out.println(
                "Rows Inserted : "
                        + todoDao.save(todo));
    }

    static void findById()
            throws SQLException {

        System.out.print("Todo Id : ");

        int id = sc.nextInt();

        System.out.println(
                todoDao.findById(id));
    }

    static void findAll()
            throws SQLException {

        todoDao.findAll()
                .forEach(System.out::println);
    }

    static void updateTodo()
            throws SQLException {

        System.out.print("Todo Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Task : ");
        String task = sc.nextLine();

        System.out.print("Status : ");
        String status = sc.nextLine();

        Todo todo =
                new Todo(id, task, status);

        System.out.println(
                "Rows Updated : "
                        + todoDao.update(todo));
    }

    static void deleteTodo()
            throws SQLException {

        System.out.print("Todo Id : ");

        int id = sc.nextInt();

        System.out.println(
                "Rows Deleted : "
                        + todoDao.deleteById(id));
    }
}