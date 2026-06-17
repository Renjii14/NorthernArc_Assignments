package controller;

import dao.FlightDao;
import entity.Flight;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Scanner;

public class FlightDaoConsoleController {

    private Scanner scanner;
    private FlightDao flightDao;

    public FlightDaoConsoleController(Scanner scanner,
                                      FlightDao flightDao) {
        this.scanner = scanner;
        this.flightDao = flightDao;
    }

    public void menu() {

        int choice;

        do {

            System.out.println("\n===== FLIGHT MANAGEMENT =====");
            System.out.println("1. Add Flight");
            System.out.println("2. Find Flight By Number");
            System.out.println("3. View All Flights");
            System.out.println("4. Update Flight");
            System.out.println("5. Delete Flight");
            System.out.println("6. Find By Source");
            System.out.println("7. Find By Destination");
            System.out.println("8. Find By Source And Destination");
            System.out.println("9. Find By Departure Date");
            System.out.println("10. Sort By Cost Asc");
            System.out.println("11. Sort By Cost Desc");
            System.out.println("12. Sort By Seats Asc");
            System.out.println("13. Sort By Seats Desc");
            System.out.println("14. Delete All Flights");
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
                case 6 -> findBySource();
                case 7 -> findByDestination();
                case 8 -> findBySourceAndDestination();
                case 9 -> findByDepartureDate();
                case 10 -> sortByCostAsc();
                case 11 -> sortByCostDesc();
                case 12 -> sortBySeatsAsc();
                case 13 -> sortBySeatsDesc();
                case 14 -> deleteAll();
                case 0 -> System.out.println("Thank You");
                default -> System.out.println("Invalid Choice");
            }

        } while (choice != 0);
    }

    private void save() {
        System.out.println("Enter Flight Number:");
        int flightNo = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Departure Date (yyyy-mm-dd):");
        Date depDate = Date.valueOf(scanner.nextLine());
        System.out.println("Enter Arrival Date (yyyy-mm-dd):");
        Date arrDate = Date.valueOf(scanner.nextLine());
        System.out.println("Enter Source:");
        String src = scanner.nextLine();
        System.out.println("Enter Destination:");
        String dest = scanner.nextLine();
        System.out.println("Enter Departure Time (HH:mm:ss):");
        Time depTime = Time.valueOf(scanner.nextLine());
        System.out.println("Enter Arrival Time (HH:mm:ss):");
        Time arrTime = Time.valueOf(scanner.nextLine());
        System.out.println("Enter Cost Per Seat:");
        double cost = scanner.nextDouble();
        System.out.println("Enter Number Of Seats:");
        int seats = scanner.nextInt();
        Flight flight = new Flight(
                flightNo,
                depDate,
                arrDate,
                src,
                dest,
                depTime,
                arrTime,
                cost,
                seats
        );
        flightDao.save(flight);
        System.out.println("Flight Added Successfully");
    }

    private void findById() {
        System.out.println("Enter Flight Number:");
        int flightNo = scanner.nextInt();
        Flight flight = flightDao.findById(flightNo);
        if (flight != null)
            System.out.println(flight);
        else
            System.out.println("Flight Not Found");
    }

    private void findAll() {
        Collection<Flight> flights = flightDao.findAll();
        flights.forEach(System.out::println);
    }

    private void update() {
        System.out.println("Enter Flight Number:");
        int flightNo = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Departure Date (yyyy-mm-dd):");
        Date depDate = Date.valueOf(scanner.nextLine());
        System.out.println("Enter Arrival Date (yyyy-mm-dd):");
        Date arrDate = Date.valueOf(scanner.nextLine());
        System.out.println("Enter Source:");
        String src = scanner.nextLine();
        System.out.println("Enter Destination:");
        String dest = scanner.nextLine();
        System.out.println("Enter Departure Time (HH:mm:ss):");
        Time depTime = Time.valueOf(scanner.nextLine());
        System.out.println("Enter Arrival Time (HH:mm:ss):");
        Time arrTime = Time.valueOf(scanner.nextLine());
        System.out.println("Enter Cost Per Seat:");
        double cost = scanner.nextDouble();
        System.out.println("Enter Number Of Seats:");
        int seats = scanner.nextInt();
        Flight flight = new Flight(
                flightNo,
                depDate,
                arrDate,
                src,
                dest,
                depTime,
                arrTime,
                cost,
                seats
        );
        flightDao.updateById(flightNo, flight);
        System.out.println("Flight Updated Successfully");
    }

    private void deleteById() {
        System.out.println("Enter Flight Number:");
        int flightNo = scanner.nextInt();
        flightDao.deleteById(flightNo);
        System.out.println("Flight Deleted Successfully");
    }

    private void findBySource() {
        System.out.println("Enter Source:");
        String src = scanner.nextLine();
        flightDao.findBySource(src)
                .forEach(System.out::println);
    }

    private void findByDestination() {
        System.out.println("Enter Destination:");
        String dest = scanner.nextLine();
        flightDao.findByDestination(dest)
                .forEach(System.out::println);
    }

    private void findBySourceAndDestination() {
        System.out.println("Enter Source:");
        String src = scanner.nextLine();
        System.out.println("Enter Destination:");
        String dest = scanner.nextLine();
        flightDao.findBySourceAndDestination(src, dest)
                .forEach(System.out::println);
    }

    private void findByDepartureDate() {
        System.out.println("Enter Departure Date (yyyy-mm-dd):");
        Date date = Date.valueOf(scanner.nextLine());
        flightDao.findByDepartureDate(date)
                .forEach(System.out::println);
    }

    private void sortByCostAsc() {
        flightDao.sortByCostAsc()
                .forEach(System.out::println);
    }

    private void sortByCostDesc() {
        flightDao.sortByCostDesc()
                .forEach(System.out::println);
    }

    private void sortBySeatsAsc() {
        flightDao.sortByAvailableSeatsAsc()
                .forEach(System.out::println);
    }

    private void sortBySeatsDesc() {
        flightDao.sortByAvailableSeatsDesc()
                .forEach(System.out::println);
    }

    private void deleteAll() {
        flightDao.deleteAll();
        System.out.println("All Flights Deleted Successfully");
    }
}