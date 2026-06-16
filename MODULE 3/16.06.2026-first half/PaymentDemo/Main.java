package PaymentDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MySpringConfigurationFactory.class);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter payment type (credit/debit/upi):");
        String paymentType = scanner.next().toLowerCase();

        System.out.println("Enter notification type (email/whatsapp):");
        String notificationType = scanner.next().toLowerCase();

        PaymentService paymentService =
                context.getBean(
                        paymentType,
                        PaymentService.class);

        NotificationService notificationService =
                context.getBean(
                        notificationType,
                        NotificationService.class);

        ExpenseManager expenseManager =
                new ExpenseManager(
                        paymentService,
                        notificationService);

//        ExpenseManager expenseManager=context.getBean(ExpenseManager.class);
        expenseManager.payElectricityBill(1000);
        expenseManager.payWaterBill(200);
        expenseManager.payGasBill(100);
    }
}