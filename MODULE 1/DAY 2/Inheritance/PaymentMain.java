import java.util.Scanner;
public class PaymentMain{
     public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the way to send amount:\n1:UPI\n2:Debit\n3:Credit");
        int value=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the amount to be sent:");
        Double amt=sc.nextDouble();
        
        switch(value){
            case 1:
                Payment m=new UPI();
                m.pay(amt);
                break;

                case 2:
                Payment m1=new Debit();
                m1.pay(amt);
                break;

                case 3:
                Payment m2=new Credit();
                m2.pay(amt);
                break;

                default:
                    System.out.println("Invalid choice");
                    return;


        }
    }
}