public class DoctorMain {
    public static void main(String[] args) {

        Doctor d = new Doctor(
                "Ramesh",
                "Cardiologist",
                12);

        d.diagnose();
        d.treat();
        d.showDetails();
    }
}