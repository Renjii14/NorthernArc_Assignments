public class Teacher {
    private String name;
    private String subject;
    private int experience;

    public Teacher(String name, String subject,
                   int experience) {
        this.name = name;
        this.subject = subject;
        this.experience = experience;
    }

    public void teach() {
        System.out.println(name + " is teaching.");
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nSUBJECT: " + subject +
                "\nEXPERIENCE: " + experience + " years");
    }
}