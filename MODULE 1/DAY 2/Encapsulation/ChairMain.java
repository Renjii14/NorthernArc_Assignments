public class ChairMain {
    public static void main(String[] args) {

        Chair c = new Chair(
                "Wood",
                "Brown",
                8.5);

        c.adjustHeight();
        c.showDetails();
    }
}