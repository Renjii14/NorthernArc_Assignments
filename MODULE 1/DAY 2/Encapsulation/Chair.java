public class Chair {
    private String material;
    private String color;
    private double weight;

    public Chair(String material, String color, double weight) {
        this.material = material;
        this.color = color;
        this.weight = weight;
    }

    public void adjustHeight() {
        System.out.println("Chair height adjusted.");
    }

    public void showDetails() {
        System.out.println("MATERIAL: " + material +
                "\nCOLOR: " + color +
                "\nWEIGHT: " + weight);
    }
}