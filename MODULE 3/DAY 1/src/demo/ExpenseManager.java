package demo;

public class ExpenseManager {

    private PaymentService ps;
    public ExpenseManager(PaymentService ps){
        this.ps=ps;
    }
    public void payElectricityBill(double amount){
        System.out.println("Paying electricity bill of "+amount);
        ps.pay(amount);
        System.out.println("Electricity bill paid.");
    }
    public void payWaterBill(double amount){
        System.out.println("Paying water bill of "+amount);
        ps.pay(amount);
        System.out.println("Water bill paid.");
    }
    public void payGasBill(double amount){
        System.out.println("Paying gas bill of "+amount);
        ps.pay(amount);
        System.out.println("Gas bill paid.");
    }

}
