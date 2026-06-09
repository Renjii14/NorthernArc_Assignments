public class Artist {
    private String name;
    private String artStyle;
    private String medium;

    public Artist(String name, String artStyle,
                  String medium) {
        this.name = name;
        this.artStyle = artStyle;
        this.medium = medium;
    }

    public void create() {
        System.out.println(name + " is creating artwork.");
    }

    public void showDetails() {
        System.out.println("NAME: " + name +
                "\nART STYLE: " + artStyle +
                "\nMEDIUM: " + medium);
    }
}