public class Laptop {
    private String brand;
    private String model;
    private String processor;
    private int ram;
    private int storage;

    public Laptop(String brand, String model, String processor,
                  int ram, int storage) {
        this.brand = brand;
        this.model = model;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
    }

    public void turnOn() {
        System.out.println(brand + " Laptop is ON");
    }

    public void turnOff() {
        System.out.println(brand + " Laptop is OFF");
    }

    public void showDetails() {
        System.out.println("BRAND: " + brand +
                "\nMODEL: " + model +
                "\nPROCESSOR: " + processor +
                "\nRAM: " + ram + " GB" +
                "\nSTORAGE: " + storage + " GB");
    }
}