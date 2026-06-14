public class RestaurantMain {
    public static void main(String[] args) {

        Restaurant r = new Restaurant(
                "A2B",
                "South Indian",
                4.3);

        r.orderFood();
        r.showDetails();
    }
}