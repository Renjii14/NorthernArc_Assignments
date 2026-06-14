package Composition.Exception;

import javax.naming.NameNotFoundException;
import java.util.Scanner;

public class customException {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the name:");
        String s=sc.next();
        try{
            if(!s.equals("Sachin")&&!s.equals("Vishnu")&&!s.equals("Lav")){
                throw new NameNotFound("Invalid Name");
            }
            System.out.println("Welcome to party!!");
        }
        catch(NameNotFound e){
            System.out.println("You are not allowed to the party: "+s);
        }
        finally{
            System.out.println("Finally block");
            sc.close();
        }
    }
}
