package Demo;
public class ExpenseManage {
    //builder design pattern used for multiple dependencies
    private PaymentService ps;
    private NotificationService ns;
    public ExpenseManage(PaymentService ps, NotificationService ns){
        this.ps=ps;
        this.ns=ns;

    }
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

