package ui;

import dao.PersonDao;
import dao.PersonDaoImpl;
import entity.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PersonMain {

    public static void main(String[] args)
            throws SQLException {

        Scanner sc = new Scanner(System.in);

        PersonDao personDao =
                new PersonDaoImpl();

        while(true) {

            System.out.println("\n1.Create");
            System.out.println("2.Find All");
            System.out.println("3.Find By Id");
            System.out.println("4.Update");
            System.out.println("5.Delete");
            System.out.println("6.Exit");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:

                    sc.nextLine();

                    System.out.println("Enter Name:");
                    String name = sc.nextLine();

                    System.out.println("Enter Email:");
                    String email = sc.nextLine();

                    System.out.println("Enter Age:");
                    float age = sc.nextFloat();

                    personDao.create(
                            new Person(name,email,age)
                    );

                    break;

                case 2:

                    List<Person> persons =
                            personDao.findAll();

                    persons.forEach(System.out::println);

                    break;

                case 3:

                    System.out.println("Enter Id:");
                    int id = sc.nextInt();

                    System.out.println(
                            personDao.findById(id)
                    );

                    break;

                case 4:

                    System.out.println("Enter Id:");
                    int uid = sc.nextInt();

                    sc.nextLine();

                    System.out.println("Enter Name:");
                    String uname = sc.nextLine();

                    System.out.println("Enter Email:");
                    String uemail = sc.nextLine();

                    System.out.println("Enter Age:");
                    float uage = sc.nextFloat();

                    personDao.update(
                            new Person(
                                    uid,
                                    uname,
                                    uemail,
                                    uage
                            )
                    );

                    break;

                case 5:

                    System.out.println("Enter Id:");
                    int did = sc.nextInt();

                    personDao.delete(did);

                    break;

                case 6:
                    System.exit(0);
            }
        }
    }
}