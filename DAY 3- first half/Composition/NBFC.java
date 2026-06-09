package Composition;

public class NBFC {

    private Loan loan;
    private Customer customer;
    private Branch branch;

    public NBFC() {
    }

    public NBFC(Loan loan, Customer customer, Branch branch) {
        this.loan = loan;
        this.customer = customer;
        this.branch = branch;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "NBFC [loan=" + loan.toString() + ", customer=" + customer + ", branch=" + branch + "]";
    }
}