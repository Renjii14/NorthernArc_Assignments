package demo;

public class UPI implements PaymentService{
    public void pay(double amount){

        System.out.println("Amount paid through upi: "+amount);
    }
}
