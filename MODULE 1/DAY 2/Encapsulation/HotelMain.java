public class HotelMain {
    public static void main(String[] args) {

        Hotel h = new Hotel(
                "Grand Palace",
                "Chennai",
                4.5);

        h.bookRoom();
        h.showDetails();
    }
}