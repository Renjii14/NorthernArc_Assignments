package Association;

public class AddressMain {
    public static void main(String[] args) {
//        PersonA p=new PersonA("Renjitha","K",address);
        Address address = new Address("123", "MG Road", "Bangalore", "Karnataka", "560001");
        PersonA p2=new PersonA("Lav","E",address);
        p2.setAddress(address);
        System.out.println(p2);
    }
}
