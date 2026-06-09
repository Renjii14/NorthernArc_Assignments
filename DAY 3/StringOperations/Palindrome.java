package StringOperations;

import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the string to be checked:");
        String s3=sc.next();
        String reverse1="";
        for(int i=s3.length()-1;i>=0;i--){
            reverse1+=s3.charAt(i);
        }
        if(reverse1.equals(s3)){
            System.out.println("It is a palindrome.");
        }
        else{
            System.out.println("Its not a palindrome.");
        }
        sc.close();
    }
}
