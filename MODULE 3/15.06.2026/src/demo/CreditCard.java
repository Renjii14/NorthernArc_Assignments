package demo;

public class CreditCard implements PaymentService{
    public void pay(double amount){
        System.out.println("Amount paid through credit card: "+amount);
    }
}
