package PaymentDemo;

public class DebitCard implements PaymentService {
    public void pay(double amount){
        System.out.println("Amount paid through debit card: "+amount);
    }
}
