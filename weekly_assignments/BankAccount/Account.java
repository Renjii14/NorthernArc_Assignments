package BankAccount;

public class Account {
    private String accOwner;
    private double amount;

    public Account(String accOwner, double amount) {
        this.accOwner = accOwner;
        this.amount = amount;
    }

    public String getAccOwner() {
        return accOwner;
    }

    public double getAmount() {
        return amount;
    }

    public void deposit(double damt) {
        amount += damt;
        System.out.println("Amount deposited successfully.");
    }

    public void withdraw(double wamt) {
        if (wamt > amount) {
            System.out.println("Insufficient balance.");
        } else {
            amount -= wamt;
            System.out.println("Amount withdrawn successfully.");
        }
    }

    public void checkBalance() {
        System.out.println("Balance amount is: " + amount);
    }
}