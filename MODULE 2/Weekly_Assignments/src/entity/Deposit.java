package entity;

public class Deposit {

    private int id;
    private String customerName;
    private double depositAmount;
    private int tenureMonths;
    private double interestRate;

    public Deposit() {
    }

    public Deposit(String customerName,
                   double depositAmount,
                   int tenureMonths,
                   double interestRate) {

        this.customerName = customerName;
        this.depositAmount = depositAmount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public Deposit(int id,
                   String customerName,
                   double depositAmount,
                   int tenureMonths,
                   double interestRate) {

        this.id = id;
        this.customerName = customerName;
        this.depositAmount = depositAmount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", depositAmount=" + depositAmount +
                ", tenureMonths=" + tenureMonths +
                ", interestRate=" + interestRate +
                '}';
    }
}