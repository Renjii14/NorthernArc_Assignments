package Association;

public class Person {
    private String fname;
    private String lname;


    public String getfname(){
        return fname;
    }

    public String getlname(){
        return lname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public Person(String fname, String lname){
        this.fname=fname;
        this.lname=lname;

    }

    public void eat(){
        System.out.println(this.fname +" "+ this.lname + " is eating");
    }

    public void age(){
        System.out.println(this.fname +" "+ this.lname );
    }

    public void walk(){
        System.out.println(this.fname +" "+ "is walking");
    }

    public void talk(){
        System.out.println(this.fname + " "+"is talking");
    }

    public String toString() {
        return "fname=" + fname + ",lname= " + lname;
    }


}
