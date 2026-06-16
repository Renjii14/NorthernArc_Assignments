package PaymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("debit")
public class DebitCard implements PaymentService {
    public void pay(double amount){
        System.out.println("Amount paid through debit card: "+amount);
    }
}
