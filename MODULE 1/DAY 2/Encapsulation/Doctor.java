public class Doctor {
    private String name;
    private String specialization;
    private int experience;

    public Doctor(String name, String specialization,
                  int experience) {
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
    }

    public void diagnose() {
        System.out.println(name + " is diagnosing a patient.");
    }

    public void treat() {
        System.out.println(name + " is treating a patient.");
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nSPECIALIZATION: " + specialization +
                "\nEXPERIENCE: " + experience + " years");
    }
}