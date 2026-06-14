public class Main11 {
    public static void main(String[] args) {
        Person e=new Manager();
        e.eat();
        e.walk();
        e.talk();
        e.work();
        e.showDetails();

        ((Manager) e).manage();

    }
}