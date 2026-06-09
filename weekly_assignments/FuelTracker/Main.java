package FuelTracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Fuel Capacity: ");
        double fuelCapacity = sc.nextDouble();

        System.out.print("Enter Mileage (km/litre): ");
        double mileage = sc.nextDouble();

        System.out.print("Enter Current Fuel Amount: ");
        double currentFuel = sc.nextDouble();

        FuelTracker f = new FuelTracker(fuelCapacity, mileage, currentFuel);

        while (true) {
            System.out.println("\nEnter the work to be done:");
            System.out.println("1. Fill Fuel");
            System.out.println("2. Check Fuel");
            System.out.println("3. Drive");
            System.out.println("4. Exit");

            int value = sc.nextInt();

            switch (value) {

                case 1:
                    System.out.print("Enter the amount of fuel to fill: ");
                    double amount = sc.nextDouble();
                    f.fillFuel(amount);
                    break;

                case 2:
                    f.checkFuel();
                    break;

                case 3:
                    System.out.print("Enter distance to drive (km): ");
                    double km = sc.nextDouble();
                    f.drive(km);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}