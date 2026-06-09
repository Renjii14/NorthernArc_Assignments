package RechargeTracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the balance amount:");
        double balance=sc.nextDouble();
        System.out.println("Enter tariff amount:");
        double tariff=sc.nextDouble();

        RecTrack r=new RecTrack(balance,tariff);
        int val;
        System.out.println("Enter function to be done: \n1.Topup \n2.Make call\n3.Check balance\n4.Exit");
        do {
        val=sc.nextInt();

            switch (val) {
                case 1:
                    System.out.println("Enter the amount to topup:");
                    int amount = sc.nextInt();
                    r.topup(amount);
                    break;

                case 2:
                    System.out.println("Enter the call duration in minutes:");
                    int minute = sc.nextInt();
                    r.makeCall(minute);
                    break;

                case 3:
                    r.checkbalance();
                    break;

                case 4:
                    System.out.println("Exittingg!!!");
                    break;

                default:
                    System.out.println("Invalid choice!!");
            }

        }while(val!=4);
        sc.close();
    }
}
