package PaymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("credit")
public class CreditCard implements PaymentService{
    public void pay(double amount){
        System.out.println("Amount paid through credit card: "+amount);
    }
}
