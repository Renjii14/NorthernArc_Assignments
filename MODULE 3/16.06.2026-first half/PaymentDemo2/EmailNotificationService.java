package PaymentDemo2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("email")
public class EmailNotificationService implements NotificationService {
    public void sendMessage(String message){

        System.out.println("Msg sent throught email.."+message);
    }
}
