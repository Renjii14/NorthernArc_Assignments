package ui;

import dao.DepositDao;
import dao.DepositDaoImpl;
import entity.Deposit;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DepositMain {

    public static void main(String[] args)
            throws SQLException {

        Scanner sc = new Scanner(System.in);

        DepositDao dao =
                new DepositDaoImpl();

        while(true) {

            System.out.println("\n1.Add Deposit");
            System.out.println("2.View All Deposits");
            System.out.println("3.Search Deposit");
            System.out.println("4.Update Deposit");
            System.out.println("5.Delete Deposit");
            System.out.println("6.Calculate Maturity");
            System.out.println("7.Exit");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:

                    sc.nextLine();

                    System.out.println("Customer Name:");
                    String name = sc.nextLine();

                    System.out.println("Amount:");
                    double amount = sc.nextDouble();

                    System.out.println("Tenure (Months):");
                    int months = sc.nextInt();

                    System.out.println("Interest Rate:");
                    double rate = sc.nextDouble();

                    dao.create(
                            new Deposit(
                                    name,
                                    amount,
                                    months,
                                    rate
                            )
                    );

                    break;

                case 2:

                    List<Deposit> deposits =
                            dao.findAll();

                    deposits.forEach(
                            System.out::println);

                    break;

                case 3:

                    System.out.println("Enter ID:");

                    int id =
                            sc.nextInt();

                    System.out.println(
                            dao.findById(id));

                    break;

                case 4:

                    System.out.println("ID:");
                    int uid = sc.nextInt();

                    sc.nextLine();

                    System.out.println("Name:");
                    String uname =
                            sc.nextLine();

                    System.out.println("Amount:");
                    double uamount =
                            sc.nextDouble();

                    System.out.println("Months:");
                    int umonths =
                            sc.nextInt();

                    System.out.println("Rate:");
                    double urate =
                            sc.nextDouble();

                    dao.update(
                            new Deposit(
                                    uid,
                                    uname,
                                    uamount,
                                    umonths,
                                    urate
                            )
                    );

                    break;

                case 5:

                    System.out.println("ID:");
                    int did =
                            sc.nextInt();

                    dao.delete(did);

                    break;

                case 6:

                    System.out.println("Deposit ID:");

                    int mid =
                            sc.nextInt();

                    Deposit d =
                            dao.findById(mid);

                    if(d != null){

                        double maturity =
                                d.getDepositAmount()
                                        +
                                        (d.getDepositAmount()
                                                * d.getInterestRate()
                                                * d.getTenureMonths())
                                                /(12*100);

                        System.out.println(
                                "Maturity Amount = "
                                        + maturity);
                    }

                    break;

                case 7:
                    System.exit(0);
            }
        }
    }
}