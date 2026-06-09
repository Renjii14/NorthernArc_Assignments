package covariant;

public class Main {
    public static void main(String[] args) {
        Person p=new Person("R","K",23);
        Student s=new Student("Renji","K",21,"Tenth","SVS");
        // System.out.println(p.getDemo());
        // System.out.println(p.getDemo());
        Person p1=p.getDemo();
        System.out.println(p1.getfname());

    }

}
