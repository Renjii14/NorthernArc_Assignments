package controller;

import dao.ProductDao;
import entity.Product;

import java.sql.SQLException;
import java.util.Scanner;

public class ProductConsoleController {
    private Scanner scanner;
    private ProductDao productDao;
    public ProductConsoleController(Scanner scanner,ProductDao productDao){
        this.scanner=scanner;
        this.productDao=productDao;

    }
    Scanner sc=new Scanner(System.in);
    public void welcome() {
        System.out.println("Welcome to Product controller");
    }
    public void showMenu() throws SQLException {
        while(true) {
            System.out.println("LIST OF FUNCTIONS FOR PRODUCT");
            System.out.println("1:Add");
            System.out.println("2:Update");
            System.out.println("3:Delete");
            System.out.println("4:List All");
            System.out.println("5.FindById");
            System.out.println("6.Exit");
            System.out.println("Enter your choice:");
            int choice= scanner.nextInt();
            redirectChoice(choice);
        }
    }
    public void redirectChoice(int choice) throws SQLException {
        switch(choice){
            case 1 -> add();
            case 2 -> update();
            case 3 -> delete();
            case 4 ->listAll();
            case 5 ->findbyId();
            default -> System.out.println("Invalid choice");
        }
    }

    private void add() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter product id");
        int id=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter product name");
        String name=sc.nextLine();
        System.out.println("Enter price");
        Double price=sc.nextDouble();

        productDao.save(new Product(id,name,price));
    }

    private void update() throws SQLException{
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter id of product to be updated");
        int id=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter product name");
        String name=sc.nextLine();
        System.out.println("Enter price");
        Double price=sc.nextDouble();

        productDao.update(id,new Product(id,name,price));
    }

    private void delete() throws SQLException{
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter id of product to be deleted");
        int id=sc.nextInt();
        productDao.deleteById(id);
    }

    private void listAll() throws SQLException{
        productDao.findAll().forEach(System.out::println);
    }

    private void findbyId() throws SQLException{
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter id of product to be found:");
        int id=sc.nextInt();
        System.out.println(productDao.findById(id));
    }
}
