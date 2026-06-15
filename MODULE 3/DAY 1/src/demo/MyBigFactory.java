package demo;


public class MyBigFactory {

    private static CreditCard creditCard = new CreditCard();
    private static DebitCard debitCard = new DebitCard();
    private static UPI upi = new UPI();

    private static EmailNotificationService emailNotification =
            new EmailNotificationService();

    private static WhatsappNotificationService whatsAppNotification =
            new WhatsappNotificationService();

    public static PaymentService getPaymentService(String paymentType) {

        if(paymentType.equalsIgnoreCase("credit")) {
            return creditCard;

        } else if(paymentType.equalsIgnoreCase("debit")) {
            return debitCard;

        } else if(paymentType.equalsIgnoreCase("upi")) {
            return upi;
        }

        return null;
    }

    public static NotificationService getNotificationService(String notificationType) {

        if (notificationType.equalsIgnoreCase("email")) {
            return (NotificationService) emailNotification;

        } else if (notificationType.equalsIgnoreCase("whatsapp")) {
            return (NotificationService) whatsAppNotification;
        }

        return null;
    }
}