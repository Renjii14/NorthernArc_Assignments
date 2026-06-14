public class SuperVillain {
    private String name;
    private String enemy;

    public String getname(String name){
        return name;
    }

    public String getenemy(String enemy){
        return enemy;
    }

    public SuperVillain(String name,String enemy){
        this.name=name;
        this.enemy=enemy;
    }

    public void name(){
        System.out.println(enemy + " is the enemy of " + name);
    }

}
