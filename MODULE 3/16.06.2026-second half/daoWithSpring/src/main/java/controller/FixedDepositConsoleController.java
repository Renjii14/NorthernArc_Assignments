package controller;

import dao.FixedDepositDao;
import entity.FixedDeposit;

import java.sql.SQLException;
import java.util.Scanner;

public class FixedDepositConsoleController {

    private Scanner scanner;
    private FixedDepositDao fixedDepositDao;
    public FixedDepositConsoleController(Scanner scanner, FixedDepositDao fixedDepositDao) {
        this.scanner = scanner;
        this.fixedDepositDao = fixedDepositDao;
    }

    public void welcome() {
        System.out.println("Welcome to Fixed Deposit Controller");
    }

    public void showMenu() throws SQLException {

        while(true) {

            System.out.println("LIST OF FUNCTIONS FOR FIXED DEPOSIT");
            System.out.println("1:Add");
            System.out.println("2:Update");
            System.out.println("3:Delete");
            System.out.println("4:List All");
            System.out.println("5:Find By Id");
            System.out.println("6:Exit");
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            redirectChoice(choice);
        }
    }

    public void redirectChoice(int choice) throws SQLException {

        switch(choice) {

            case 1 -> add();
            case 2 -> update();
            case 3 -> delete();
            case 4 -> listAll();
            case 5 -> findById();
            case 6 -> System.exit(0);
            default -> System.out.println("Invalid choice");
        }
    }

    private void add() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter FD Id");
        int fdId = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Customer Name");
        String customerName = sc.nextLine();
        System.out.println("Enter Deposit Amount");
        double depositAmount = sc.nextDouble();
        System.out.println("Enter Interest Rate");
        double interestRate = sc.nextDouble();
        System.out.println("Enter Tenure (Months)");
        int tenureMonths = sc.nextInt();
        fixedDepositDao.save(new FixedDeposit(fdId, customerName, depositAmount, interestRate, tenureMonths));
    }

    private void update() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter FD Id to update");
        int fdId = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Customer Name");
        String customerName = sc.nextLine();
        System.out.println("Enter Deposit Amount");
        double depositAmount = sc.nextDouble();
        System.out.println("Enter Interest Rate");
        double interestRate = sc.nextDouble();
        System.out.println("Enter Tenure (Months)");
        int tenureMonths = sc.nextInt();
        fixedDepositDao.update(fdId, new FixedDeposit(fdId, customerName, depositAmount, interestRate, tenureMonths));
    }

    private void delete() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter FD Id to delete");
        int fdId = sc.nextInt();
        fixedDepositDao.deleteById(fdId);
    }

    private void listAll() throws SQLException {

        fixedDepositDao.findAll().forEach(System.out::println);
    }

    private void findById() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter FD Id to find");
        int fdId = sc.nextInt();
        System.out.println(fixedDepositDao.findById(fdId));
    }
}