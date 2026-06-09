package StringOperations;

import java.util.Scanner;

public class Reverse {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the string to reverse:");
        String s=sc.next();
        String reverse="";
        for(int i=s.length()-1;i>=0;i--){
            reverse+=s.charAt(i);

        }
        System.out.println("Reverse: " + reverse);
    }
    }

