package StringOperations;

import java.util.Scanner;

public class Whitespace {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the sentence:");
        String s=sc.nextLine();
        s.trim();
        int spacecount=0;
        int wordcount=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==' '){
                spacecount++;
            }
        }
        System.out.println("Whitespace: "+spacecount);
        System.out.println("WordCount: "+spacecount+1);
    }
}
