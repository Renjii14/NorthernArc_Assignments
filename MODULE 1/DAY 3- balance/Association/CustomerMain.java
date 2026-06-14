package Association;

public class CustomerMain {

    public static void main(String[] args) {

        Customer c = new Customer("Renjitha", "C101");

        Loan l = new Loan("L5001", 500000);

        l.setCustomer(c);

        System.out.println(l);
    }
}