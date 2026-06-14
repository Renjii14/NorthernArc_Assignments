package upcast;

public class Main {
    public static void main(String[] args) {
        Greeting g=new Greeting();
        Person p=new Person("Renjitha","K",21);
        Student s=new Student("R","K",21,"Tenth","SVS");
        Employee e=new Employee("Renji","K",21,"Associate",30000);

        g.greet(null);
    }


}
