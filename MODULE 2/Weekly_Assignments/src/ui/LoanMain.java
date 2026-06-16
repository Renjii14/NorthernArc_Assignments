package ui;

import dao.LoanDao;
import dao.LoanDaoImpl;
import entity.Loan;

import java.sql.SQLException;
import java.util.Scanner;

public class LoanMain {

    private static Scanner sc =
            new Scanner(System.in);

    private static LoanDao loanDao =
            new LoanDaoImpl();

    public static void main(String[] args)
            throws SQLException {

        while(true) {

            System.out.println("\n===== LOAN MENU =====");

            System.out.println("1. Add Loan");
            System.out.println("2. Find By Id");
            System.out.println("3. Find All");
            System.out.println("4. Update Loan");
            System.out.println("5. Delete Loan");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1 -> addLoan();

                case 2 -> findById();

                case 3 -> findAll();

                case 4 -> updateLoan();

                case 5 -> deleteLoan();

                default -> {
                    System.out.println("Thank You");
                    return;
                }
            }
        }
    }

    static void addLoan()
            throws SQLException {

        System.out.print("Loan Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Customer Name : ");
        String name = sc.nextLine();

        System.out.print("Amount : ");
        double amount = sc.nextDouble();

        Loan loan =
                new Loan(id,name,amount);

        System.out.println(
                "Rows Inserted : "
                        + loanDao.save(loan));
    }

    static void findById()
            throws SQLException {

        System.out.print("Loan Id : ");

        int id = sc.nextInt();

        System.out.println(
                loanDao.findById(id));
    }

    static void findAll()
            throws SQLException {

        loanDao.findAll()
                .forEach(System.out::println);
    }

    static void updateLoan()
            throws SQLException {

        System.out.print("Loan Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Customer Name : ");
        String name = sc.nextLine();

        System.out.print("Amount : ");
        double amount = sc.nextDouble();

        Loan loan =
                new Loan(id,name,amount);

        System.out.println(
                "Rows Updated : "
                        + loanDao.update(loan));
    }

    static void deleteLoan()
            throws SQLException {

        System.out.print("Loan Id : ");

        int id = sc.nextInt();

        System.out.println(
                "Rows Deleted : "
                        + loanDao.deleteById(id));
    }
}