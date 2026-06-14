public class LaptopMain {
    public static void main(String[] args) {

        Laptop l = new Laptop(
                "Dell",
                "Inspiron",
                "Intel i5",
                16,
                512);

        l.turnOn();
        l.showDetails();
        l.turnOff();
    }
}