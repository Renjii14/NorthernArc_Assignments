package todo.main;

import config.TodoSpringConfig;
import todo.dao.TodoDao;
import todo.entity.Todo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.Scanner;

public class TodoMainJdbc {
    private static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) throws SQLException {
        System.out.println("Todo With Spring Config , JDBC");

        ApplicationContext context=new AnnotationConfigApplicationContext(TodoSpringConfig.class);
        TodoDao todoDao=context.getBean("todojdbc",TodoDao.class);

        System.out.println("1.save ");
        System.out.println("2.findById ");
        System.out.println("3.findAll ");
        System.out.println("4.deleteById ");
        System.out.println("5.updateById ");

        do {
            System.out.println("Enter option");
            int op= sc.nextInt();

            switch (op){
                case 1-> {
                    System.out.println("Enter id");
                    int id=sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter title");

                    String title=sc.nextLine();
                    System.out.println("Enter description");
                    String description=sc.nextLine();
                    System.out.println("Enter completed(boolean)");
                    Boolean completed=sc.nextBoolean();
                    todoDao.save(new Todo(id, title, description, completed));
                }
                case 2->{
                    System.out.println("Enter id");
                    int id=sc.nextInt();
                    System.out.println(todoDao.findById(id));

                }
                case 3->{
                    todoDao.findAll().forEach(System.out::println);
                }
                case 4->{
                    System.out.println("Enter id");
                    int id=sc.nextInt();
                    todoDao.deleteById(id);

                }
                case 5->{
                    System.out.println("Enter id");
                    int id=sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter title");
                    String title=sc.nextLine();
                    System.out.println("Enter description");
                    String description=sc.nextLine();
                    System.out.println("Enter completed(boolean)");
                    Boolean completed=sc.nextBoolean();
                    todoDao.updateById(id,new Todo(id, title, description, completed));
                }
                default -> {throw new IllegalArgumentException("Invalid option");}
            }

        }while(true);

    }
}

