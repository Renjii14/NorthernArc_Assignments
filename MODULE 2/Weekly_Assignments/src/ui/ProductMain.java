package ui;

import dao.ProductDao;
import dao.ProductDaoImpl;
import entity.Product;

import java.sql.SQLException;
import java.util.Scanner;

public class ProductMain {

    private static Scanner sc =
            new Scanner(System.in);

    private static ProductDao productDao =
            new ProductDaoImpl();

    public static void main(String[] args)
            throws SQLException {

        while(true) {

            System.out.println("\n===== PRODUCT MENU =====");

            System.out.println("1. Add Product");
            System.out.println("2. Find By Id");
            System.out.println("3. Find All Products");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1 -> addProduct();

                case 2 -> findById();

                case 3 -> findAll();

                case 4 -> updateProduct();

                case 5 -> deleteProduct();

                default -> {
                    System.out.println("Thank You");
                    return;
                }
            }
        }
    }

    static void addProduct()
            throws SQLException {

        System.out.print("Product Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Product Name : ");
        String name = sc.nextLine();

        System.out.print("Price : ");
        double price = sc.nextDouble();

        Product product =
                new Product(id,name,price);

        System.out.println(
                "Rows Inserted : "
                        + productDao.save(product));
    }

    static void findById()
            throws SQLException {

        System.out.print("Product Id : ");

        int id = sc.nextInt();

        System.out.println(
                productDao.findById(id));
    }

    static void findAll()
            throws SQLException {

        productDao.findAll()
                .forEach(System.out::println);
    }

    static void updateProduct()
            throws SQLException {

        System.out.print("Product Id : ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Product Name : ");
        String name = sc.nextLine();

        System.out.print("Price : ");
        double price = sc.nextDouble();

        Product product =
                new Product(id,name,price);

        System.out.println(
                "Rows Updated : "
                        + productDao.update(product));
    }

    static void deleteProduct()
            throws SQLException {

        System.out.print("Product Id : ");

        int id = sc.nextInt();

        System.out.println(
                "Rows Deleted : "
                        + productDao.deleteById(id));
    }
}