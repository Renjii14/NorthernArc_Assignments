package RechargeTracker;

public class RecTrack {
    private double balance;
    private double tariff;

    public RecTrack(double balance,double tariff){
        this.balance=balance;
        this.tariff=tariff;

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double tariff) {
        this.tariff = tariff;
    }

    public void topup(int amt){
        balance+=amt;
        System.out.println("Recharge done successfully");
    }

    public void makeCall(int min){
        double taramt;
        taramt=2*min;
        balance-=taramt;
        if(taramt>balance){
            System.out.println("Less Balance!!Cannot make call.");
        }
        else{
            System.out.println("Making Call!!!");
        }

    }

    public void checkbalance(){
        System.out.println("Balance: "+balance);
    }
}
