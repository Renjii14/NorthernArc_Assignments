public class Bulb {
    private int watts;
    private String brand;

    public LightBulb(int watts, String brand) {
        this.watts = watts;
        this.brand = brand;
    }

    public void turnOn() {
        System.out.println("Light Bulb ON");
    }

    public void turnOff() {
        System.out.println("Light Bulb OFF");
    }

    public void showDetails() {
        System.out.println("WATTS: " + watts +
                "\nBRAND: " + brand);
    }
}