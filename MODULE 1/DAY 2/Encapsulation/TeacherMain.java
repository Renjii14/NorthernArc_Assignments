public class TeacherMain {
    public static void main(String[] args) {

        Teacher t = new Teacher(
                "Priya",
                "Mathematics",
                8);

        t.teach();
        t.showDetails();
    }
}