public class Superhero{
    private String name;
    private String superpower;

    public String getname(){
        return name;
    }

    public String getsuperpower(){
        return superpower;
    }

    public Superhero(String name,String superpower){
        this.name=name;
        this.superpower=superpower;
    }

    public void useSuperPower(){
        System.out.println(name + "uses" + superpower);
    }

    public void saveWorld(){
        System.out.println(name + "saves the world");
    }

    public static void saveTheWorld(){
        System.out.println("All superheroes saves the world");
    }
}