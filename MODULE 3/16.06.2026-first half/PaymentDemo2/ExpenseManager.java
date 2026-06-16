package PaymentDemo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExpenseManager {

        @Autowired
        private PaymentService ps;

        @Autowired
        private NotificationService ns;

        public void payElectricityBill(double amount){
        System.out.println("Paying electricity bill of "+amount);
        ps.pay(amount);
        ns.sendMessage("Electricity bill paid of amount:" +amount);
        System.out.println("Electricity bill paid.");
    }
        public void payWaterBill(double amount){
        System.out.println("Paying water bill of "+amount);
        ps.pay(amount);
        ns.sendMessage("Water bill paid of amount:" +amount);
        System.out.println("Water bill paid.");
    }
        public void payGasBill(double amount){
        System.out.println("Paying gas bill of "+amount);
        ps.pay(amount);
        ns.sendMessage("Gas bill paid of amount:" +amount);
        System.out.println("Gas bill paid.");
    }

    }
