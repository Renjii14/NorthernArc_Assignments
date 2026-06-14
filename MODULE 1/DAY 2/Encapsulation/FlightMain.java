public class FlightMain{
    public static void main(String[] args){
        Flight f = new Flight("IndiGo", 1234, "Chennai", "Mumbai", "2026-06-03 08:30", "2026-06-03 10:45");
        f.getstatus();
        f.showDetails();
    }
}