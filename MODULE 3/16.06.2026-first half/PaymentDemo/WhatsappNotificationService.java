package PaymentDemo;
public class WhatsappNotificationService implements NotificationService{
    public void sendMessage(String message){
        System.out.println("Msg sent throught whatsapp.."+message);
    }
}
