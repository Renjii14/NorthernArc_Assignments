public class Person {
    private String fname;
    protected  String lname;
    protected  int age;

    public String getfname(){
        return fname;
    }

    public String getlname(){
        return lname;
    }

    public int getage(){
        return age;
    }


    public Person(String fname,String lname,int age){
        this.fname=fname;
        this.lname=lname;
        this.age=age;
    }

    public void eat(){
        System.out.println(this.fname +" "+ this.lname + " is eating");
    }
    
    public void age(){
        System.out.println(this.fname +" "+ this.lname + " age is" + age);
    }
    

    public void walk(){
        System.out.println(this.fname +" "+ "is walking");
    }

    public void talk(){
        System.out.println(this.fname + " "+"is talking");
    }

    public void showDetails(){
        System.out.println("NAME: "+fname+" "+lname);
        System.out.println("AGE: "+age);
    }
}
