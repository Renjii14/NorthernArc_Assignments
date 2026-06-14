package StringOperations;

import java.util.Locale;
import java.util.Scanner;

public class Vowel {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the string to count vowels:");
        String s=sc.next().toLowerCase();
        int vowelcount=0;
        for(int i=0;i<s.length();i++){
            char ch=s.charAt(i);
            if(ch=='a'||ch=='e'||ch=='o'||ch=='i'||ch=='u'){
                vowelcount++;
            }

        }
        System.out.println(vowelcount);
    }
}
