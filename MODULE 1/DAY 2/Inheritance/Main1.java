public class Main1 {
    public static void main(String[] args) {
        Employee e=new Employee();
        e.getfname();
        e.setter("Renjitha", "K", 21);
        e.setempid(101);
        e.setdept("HR");

        e.eat();
        e.walk();
        e.talk();
        e.work();
//upcasting
        Person emp=new Employee();
        emp.setter("Lavanya", "E", 21);
        
//downcasting
        ((Employee) emp).setempid(102);
        ((Employee) emp).showDetails();

        Person p=new Student();
        p.setter("Tenth","Svs");

        ((Student) p).study();

        
        



    }
}
