package Association;

public class Loan {

    private String loanId;
    private double amount;
    private Customer customer;

    public Loan(String loanId, double amount) {
        this.loanId = loanId;
        this.amount = amount;
    }

    public String getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", amount=" + amount +
                ", customer=" + customer +
                '}';
    }
}