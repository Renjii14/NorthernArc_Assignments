package entity;

public class Loan {

    private int loanId;
    private String customerName;
    private double amount;

    public Loan() {
    }

    public Loan(int loanId, String customerName, double amount) {
        this.loanId = loanId;
        this.customerName = customerName;
        this.amount = amount;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Loan [loanId=" + loanId +
                ", customerName=" + customerName +
                ", amount=" + amount + "]";
    }
}