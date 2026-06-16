package demo;

import demo.PaymentService;

import java.util.Scanner;

public class FactoryMain {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter payment type: credit/debit/upi");
        String paymentType = scanner.next();

        System.out.println("Enter notification type: email/whatsapp");
        String notificationType = scanner.next();

        PaymentService paymentService =
                MyBigFactory.getPaymentService(paymentType);

        NotificationService notificationService =
                MyBigFactory.getNotificationService(notificationType);

        ExpenseManagerConstructor expenseManager =
                new ExpenseManagerConstructor(
                        paymentService,
                        notificationService);

        expenseManager.payElectricityBill(1000);
        expenseManager.payWaterBill(200);
        expenseManager.payGasBill(100);
    }
}