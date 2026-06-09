import java.util.Scanner;

public class Loops {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Even number range:");
        int range1=sc.nextInt();
        EvenRange(range1);

        System.out.println("\nEnter Odd number range:");
        int range2=sc.nextInt();
        OddRange(range2);

        System.out.println("Enter number for multiplaction table:");
        int num=sc.nextInt();
        Multiply(num);

        System.out.println("Enter number to find its factors:");
        int n=sc.nextInt();
        Factors(n);

        System.out.println("Enter fibonacci number:");
        int n1=sc.nextInt();
        Fibonacci(n1);

        System.out.println("Enter number for factorial:");
        int n2=sc.nextInt();
        factorial(n2);

        System.out.println("Enter number to check prime or not:");
        int n3=sc.nextInt();
        prime(n3);


      
    }

    public static void EvenRange(int range){
        for(int i=0;i<=range;i++){
            if(i%2==0){
                System.out.print(i+" ");
            }
        }
    }

    public static void OddRange(int range){
        for(int i=0;i<=range;i++){
            if(i%2!=0){
                System.out.print(i+" ");
            }
        }
    }

    public static void Multiply(int number){
        for(int i=1;i<=10;i++){
            System.out.println(i + "*" + number + "=" + i*number);
        }
    }

    public static void Factors(int n){
        int count=0;
        int sum=0;
        for(int i=1;i<=n;i++){
            if(n%i==0){
                System.out.println(i);
                count++;
                sum=sum+i;
            }
        }
       System.out.println("\nCount of factors:"+count);
       System.out.println("\nSum of factors:"+ sum);
    }

   public static void Fibonacci(int n){
    int a=0,b=1;
    int c;
    for(int i=1;i<=n;i++){
        System.out.println(a);
        c=a+b;
        a=b;
        b=c;
    }
   }

   public static void factorial(int n) {
    int fact = 1;

    for (int i = 1; i <= n; i++) {
        fact = fact * i;
    }
    System.out.println(fact);

}

public static void prime(int n) {
    if (n <= 1) {
        System.out.println("Not Prime");
        return;
    }

    for (int i = 2; i < n; i++) {
        if (n % i == 0) {
            System.out.println("Not Prime");
            return;
        }
    }

    System.out.println("Prime");
}

}