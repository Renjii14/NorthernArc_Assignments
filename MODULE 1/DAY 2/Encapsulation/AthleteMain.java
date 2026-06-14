public class AthleteMain {
    public static void main(String[] args) {

        Athlete a = new Athlete(
                "Rahul",
                "Cricket",
                "India");

        a.train();
        a.compete();
        a.showDetails();
    }
}