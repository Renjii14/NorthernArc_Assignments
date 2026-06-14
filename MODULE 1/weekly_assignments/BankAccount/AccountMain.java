package BankAccount;

import java.util.Scanner;

public class AccountMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        System.out.print("Enter Account Holder Name: ");
        String name = sc.next();

        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        Account acc = new Account(name, balance);

        int choice;

        do {
            System.out.println("\n----- BANK MENU -----");
            System.out.println("1. Deposit Amount");
            System.out.println("2. Withdraw Amount");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depAmt = sc.nextDouble();
                    acc.deposit(depAmt);
                    break;

                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withAmt = sc.nextDouble();
                    acc.withdraw(withAmt);
                    break;

                case 3:
                    acc.checkBalance();
                    break;

                case 4:
                    System.out.println("Thank you for using the banking system.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }
}