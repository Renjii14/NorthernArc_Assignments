package Demo;

public class EmailNotificationService implements NotificationService {
    public void sendMessage(String message){

        System.out.println("Msg sent throught email.."+message);
    }
}