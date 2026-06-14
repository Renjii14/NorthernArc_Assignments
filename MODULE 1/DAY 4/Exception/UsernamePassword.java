package Composition.Exception;

import java.util.Scanner;

public class UsernamePassword {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Username:");
        String name=sc.next();
        System.out.println("Enter password:");
        String pwd=sc.next();
        try{
            if(!name.equals("Sachin")&&!pwd.equals("123S")){
                throw new InvalidCredentials("Invalid Credentials");
            }
            System.out.println("Welcome to website!!");
        }
        catch(InvalidCredentials e){
            System.out.println("You are not allowed to the login: "+name);
        }
    }
}
