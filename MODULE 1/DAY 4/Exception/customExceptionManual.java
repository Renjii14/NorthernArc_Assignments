package Composition.Exception;

import java.util.Scanner;

public class customExceptionManual {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the name:");
        String s=sc.next();
        try{
            if(!s.equals("Sachin")&&!s.equals("Vishnu")&&!s.equals("Lav")){
                throw new ArithmeticException("Invalid name");
            }
            System.out.println("Welcome to party!!");
        }
        catch(ArithmeticException e){
            System.out.println("You are not allowed to the party: "+s);
        }
        finally{
            System.out.println("Finally block");
            sc.close();
        }
    }
}
