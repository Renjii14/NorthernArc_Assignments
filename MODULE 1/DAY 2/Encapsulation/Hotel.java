public class Hotel {
    private String name;
    private String location;
    private double rating;

    public Hotel(String name, String location,
                 double rating) {
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    public void bookRoom() {
        System.out.println("Room booked in " + name);
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nLOCATION: " + location +
                "\nRATING: " + rating);
    }
}