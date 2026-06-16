package demo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter amount:");
        double amt = sc.nextDouble();

        System.out.println("Choose Payment Method");
        System.out.println("1. UPI");
        System.out.println("2. Debit Card");
        System.out.println("3. Credit Card");

        int n = sc.nextInt();

        PaymentService pm;

        switch (n) {

            case 1:
                pm = new UPI();
                break;

            case 2:
                pm = new DebitCard();
                break;

            case 3:
                pm = new CreditCard();
                break;

            default:
                System.out.println("Invalid Choice");
                sc.close();
                return;
        }

        // Constructor Injection
        ExpenseManager expenseManager = new ExpenseManager(pm);

        System.out.println("\nChoose Bill Type");
        System.out.println("1. Electricity Bill");
        System.out.println("2. Water Bill");
        System.out.println("3. Gas Bill");

        int billChoice = sc.nextInt();

        switch (billChoice) {

            case 1:
                expenseManager.payElectricityBill(amt);
                break;

            case 2:
                expenseManager.payWaterBill(amt);
                break;

            case 3:
                expenseManager.payGasBill(amt);
                break;

            default:
                System.out.println("Invalid Bill Type");
        }

        sc.close();
    }
}