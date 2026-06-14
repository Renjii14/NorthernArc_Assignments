package Association;

public class PersonMain {
    public static void main(String[] args) {
        Person p=new Person("Renjitha","K");
        Passport pass=new Passport("12345678","India","14-10-2004","12-8-2026");
        pass.setPerson(p);
        System.out.println(pass);
    }
}
