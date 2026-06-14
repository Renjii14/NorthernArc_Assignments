public class Athlete {
    private String name;
    private String sport;
    private String team;

    public Athlete(String name, String sport,
                   String team) {
        this.name = name;
        this.sport = sport;
        this.team = team;
    }

    public void train() {
        System.out.println(name + " is training.");
    }

    public void compete() {
        System.out.println(name + " is competing.");
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nSPORT: " + sport +
                "\nTEAM: " + team);
    }
}