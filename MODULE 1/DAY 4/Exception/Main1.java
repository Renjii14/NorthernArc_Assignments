package Composition.Exception;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter number 1:");
            int a = sc.nextInt();
            System.out.println("Enter number 2:");
            int b = sc.nextInt();
            double divide = a / b;
            String s = null;
            System.out.println(s.length());
        }
        catch(ArithmeticException e){
            System.out.println("Division by zero not possible");
        }
        catch(InputMismatchException e){
            System.out.println("Enter valid one.");
        }
        catch(NullPointerException e){
            System.out.println("Value is null");
        }
        catch(Exception e){
            System.out.println("Something went wrong:"+e);
        }
        finally{
            System.out.println("Final block");
            sc.close();
        }
        System.out.println("Hello World");
    }
}
