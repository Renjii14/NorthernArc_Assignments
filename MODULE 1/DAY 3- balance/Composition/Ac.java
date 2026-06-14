package Composition;

public class Ac {
    private String brand;

    public Ac(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Ac [brand=" + brand + "]";
    }
}
