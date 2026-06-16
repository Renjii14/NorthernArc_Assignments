package PaymentDemo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySpringConfigurationFactory {

    @Bean("credit")
    public PaymentService creditCard() {
        return new CreditCard();
    }

    @Bean("debit")
    public PaymentService debitCard() {
        return new DebitCard();
    }

    @Bean("upi")
    public PaymentService upi() {
        return new UPI();
    }

    @Bean("email")
    public NotificationService email() {
        return new EmailNotificationService();
    }

    @Bean("whatsapp")
    public NotificationService whatsapp() {
        return new WhatsappNotificationService();
    }
    //similarly we need to create many expensemanager for all combinations and in main use getBean to implement expenseManager
//    @Bean
//    public ExpenseManager expenseManager(@Qualifier("upi") PaymentService paymentService,@Qualifier("email") NotificationService notificationService){
//        return new ExpenseManager(paymentService,notificationService);
//    }
}