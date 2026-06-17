package com.northernArc.firstbootapp.controller;

import com.northernArc.firstbootapp.dao.TodoDao;
import com.northernArc.firstbootapp.model.Todo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Scanner;
@Service
public class TodoDaoConsoleController {
    @Autowired
    private Scanner scanner;
    @Autowired
    private TodoDao todoDao;
        public void printWelcomeMessage(){
            System.out.println("Welcome to Todo Dao service..");
        }
        public void showMenu() throws SQLException {
            while(true) {
                System.out.println("1:Add");
                System.out.println("2:Update");
                System.out.println("3:Delete");
                System.out.println("4:List All");
                System.out.println("Enter your choice:");
                int choice = scanner.nextInt();
                redirectChoice(choice);
            }
        }
        private void redirectChoice(int choice) throws SQLException {
            switch(choice){
                case 1:
                    add();
                    break;
                case 2:
                    update();
                    break;
                case 3:
                    delete();
                case 4:
                    listAll();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        private void listAll() throws SQLException {
            System.out.println(todoDao.findAll());
        }
        private void delete() throws SQLException {
            System.out.println("Enter id of task to be deleted:");
            int id=scanner.nextInt();
            todoDao.deleteById(id);
        }
        private void update() throws SQLException {

            System.out.println("Enter id of task to be updated:");
            int id=scanner.nextInt();

            scanner.nextLine();

            System.out.println("Enter new content:");
            String task=scanner.nextLine();

            System.out.println("Enter new done status:");
            boolean status=scanner.nextBoolean();

            todoDao.update(
                    id,
                    new Todo(id,task,status)
            );
        }
        private void add() throws SQLException {
            System.out.println("Enter id:");
            int id=scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter content:");
            String content=scanner.nextLine();
            System.out.println("Enter done:");
            boolean done=scanner.nextBoolean();
            todoDao.save(new Todo(id,content,done));
        }


}
