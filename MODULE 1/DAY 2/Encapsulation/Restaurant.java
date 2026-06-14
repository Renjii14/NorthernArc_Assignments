public class Restaurant {
    private String name;
    private String cuisine;
    private double rating;

    public Restaurant(String name, String cuisine,
                      double rating) {
        this.name = name;
        this.cuisine = cuisine;
        this.rating = rating;
    }

    public void orderFood() {
        System.out.println("Food ordered from " + name);
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nCUISINE: " + cuisine +
                "\nRATING: " + rating);
    }
}