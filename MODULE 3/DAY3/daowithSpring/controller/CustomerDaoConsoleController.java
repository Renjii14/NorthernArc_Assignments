package controller;

import dao.CustomerDao;
import entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Scanner;
@Component
public class CustomerDaoConsoleController {

    private Scanner scanner;
    private CustomerDao customerDao;
@Autowired
    public CustomerDaoConsoleController(Scanner scanner,
                                     CustomerDao customerDao) {
        this.scanner = scanner;
        this.customerDao = customerDao;
    }

    public void showMenu() {

        int choice;

        do {
            System.out.println("\n===== CUSTOMER MANAGEMENT =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Find Customer By Id");
            System.out.println("3. View All Customers");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Find By Name");
            System.out.println("7. Find By City");
            System.out.println("8. Find By City And Name");
            System.out.println("9. Sort By Name Asc");
            System.out.println("10. Sort By Name Desc");
            System.out.println("11. Delete All Customers");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> save();
                case 2 -> findById();
                case 3 -> findAll();
                case 4 -> update();
                case 5 -> deleteById();
                case 6 -> findByName();
                case 7 -> findByCity();
                case 8 -> findByCityAndName();
                case 9 -> sortByNameAsc();
                case 10 -> sortByNameDesc();
                case 11 -> deleteAll();
                case 0 -> System.out.println("Thank You");
                default -> System.out.println("Invalid Choice");
            }

        } while (choice != 0);
    }

    private void save() {

        System.out.println("Enter Customer Id:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();

        System.out.println("Enter City:");
        String city = scanner.nextLine();

        System.out.println("Enter Mobile Number:");
        String mobile = scanner.nextLine();

        System.out.println("Enter Email:");
        String email = scanner.nextLine();

        Customer customer =
                new Customer(id, name, city, mobile, email);

        customerDao.save(customer);

        System.out.println("Customer Added Successfully");
    }

    private void findById() {

        System.out.println("Enter Customer Id:");
        int id = scanner.nextInt();

        Customer customer = customerDao.findById(id);

        if (customer != null)
            System.out.println(customer);
        else
            System.out.println("Customer Not Found");
    }

    private void findAll() {

        Collection<Customer> customers =
                customerDao.findAll();

        customers.forEach(System.out::println);
    }

    private void update() {

        System.out.println("Enter Customer Id:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();

        System.out.println("Enter City:");
        String city = scanner.nextLine();

        System.out.println("Enter Mobile Number:");
        String mobile = scanner.nextLine();

        System.out.println("Enter Email:");
        String email = scanner.nextLine();

        Customer customer =
                new Customer(id, name, city, mobile, email);

        customerDao.updateById(id, customer);

        System.out.println("Customer Updated Successfully");
    }

    private void deleteById() {

        System.out.println("Enter Customer Id:");
        int id = scanner.nextInt();

        customerDao.deleteById(id);

        System.out.println("Customer Deleted Successfully");
    }

    private void findByName() {

        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();

        customerDao.findByName(name)
                .forEach(System.out::println);
    }

    private void findByCity() {

        System.out.println("Enter City:");
        String city = scanner.nextLine();

        customerDao.findByCity(city)
                .forEach(System.out::println);
    }

    private void findByCityAndName() {

        System.out.println("Enter City:");
        String city = scanner.nextLine();

        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();

        customerDao.findByCityAndName(city, name)
                .forEach(System.out::println);
    }

    private void sortByNameAsc() {

        customerDao.sortByNameAsc()
                .forEach(System.out::println);
    }

    private void sortByNameDesc() {

        customerDao.sortByNameDesc()
                .forEach(System.out::println);
    }

    private void deleteAll() {

        customerDao.deleteAll();

        System.out.println("All Customers Deleted Successfully");
    }
}