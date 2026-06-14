public class BulbMain {
    public static void main(String[] args) {

        LightBulb b = new LightBulb(
                60,
                "Philips");

        b.turnOn();
        b.showDetails();
        b.turnOff();
    }
}