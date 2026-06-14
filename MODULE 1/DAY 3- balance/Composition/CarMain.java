package Composition;

public class CarMain {

    public static void main(String[] args) {

        Car c = new Car(
                new Engine(50),
                new Music("1"),
                new Ac("HW")
        );

        System.out.println(c);
    }
}