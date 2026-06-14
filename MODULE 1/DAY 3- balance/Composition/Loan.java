package Composition;

public class Loan {

    private double amount;

    public Loan() {
    }

    public Loan(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Loan [amount=" + amount + "]";
    }
}