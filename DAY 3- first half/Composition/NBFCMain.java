package Composition;

public class NBFCMain {

    public static void main(String[] args) {

        NBFC n = new NBFC(
                new Loan(500000),
                new Customer("Renjitha"),
                new Branch("Chennai")
        );

        System.out.println(n);
    }
}