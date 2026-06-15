package SetterInjection;

import demo.NotificationService;
import demo.PaymentService;

public class ExpenseManagerSetter {

    private PaymentService ps;
    private NotificationService ns;

    // Setter Injection
    public void setPaymentService(PaymentService ps) {
        this.ps = ps;
    }

    public void setNotificationService(NotificationService ns) {
        this.ns = ns;
    }

    public void payElectricityBill(double amount) {
        System.out.println("Paying electricity bill of " + amount);

        ps.pay(amount);

        ns.sendMessage(
                "Electricity bill payment successful. Amount: " + amount);

        System.out.println("Electricity bill paid.");
    }

    public void payWaterBill(double amount) {
        System.out.println("Paying water bill of " + amount);

        ps.pay(amount);

        ns.sendMessage(
                "Water bill payment successful. Amount: " + amount);

        System.out.println("Water bill paid.");
    }

    public void payGasBill(double amount) {
        System.out.println("Paying gas bill of " + amount);

        ps.pay(amount);

        ns.sendMessage(
                "Gas bill payment successful. Amount: " + amount);

        System.out.println("Gas bill paid.");
    }
}