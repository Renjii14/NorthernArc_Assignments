import java.util.Scanner;

public class Conditional {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter age:");
        int age=sc.nextInt();
        Age(age);

        System.out.println("Enter number:");
        int num=sc.nextInt();
        PosNeg(num);

        System.out.println("Enter year:");
        int year=sc.nextInt();
        Leap(year);

        System.out.println("Enter number1:");
        int a=sc.nextInt();
        System.out.println("Enter number2:");
        int b=sc.nextInt();
        System.out.println("Enter number3:");
        int c=sc.nextInt();
        Large(a,b,c);

        System.out.println("Enter mark:");
        int mark=sc.nextInt();
        MARK(mark);

         System.out.println("Enter number:");
        int num1=sc.nextInt();
        EVENODD(num1);

        
}

private static void Age(int age){
    if(age>=18){
            System.out.println("Eligible for voting");
        }
        else{
            System.out.println("Not Eligible for voting");
        }
}

  private static void PosNeg(int num){
        if(num>0){
            System.out.println("Positive");
        }
        else{
            System.out.println("Negative");
        }
    }

    private static void Leap(int year){
    if(year%400==0){
        System.out.println("LEAP YEAR");
            }
       else if(year%100==0){
            System.out.println("NOT LEAP YEAR");
        }
        else if(year%4==0){
                System.out.println("LEAP YEAR");
            }
            else{
                System.out.println("NOT LEAP YEAR");
            }
        }

        private static void Large(int a,int b,int c){
         if(a>b&&b>c){
            System.out.println(a);
        }
        else if(b>a&&b>c){
            System.out.println(b);
        }
        else
        {
            System.out.println(c);
        }
    }

    private static void MARK(int mark){
    if(mark>85 && mark<=100){
            System.out.println("A Grade");
        }
        else if(mark>70 && mark<=85){
            System.out.println("B Grade");
        }
        else if(mark>55 && mark<=70){
            System.out.println("C Grade");
        }
        else {
            System.out.println("FAIL");
        }
}

private static void EVENODD(int number){
    if(number%2==0){
            System.out.println("EVEN");
        }
        else{
            System.out.println("ODD");
        }
}
}

