package PaymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("upi")
public class UPI implements PaymentService{
    public void pay(double amount){

        System.out.println("Amount paid through upi: "+amount);
    }
}
