package Association;

public class PersonA {
    private String fname;
    private String lname;
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PersonA(String fname, String lname,Address address){
        this.fname=fname;
        this.lname=lname;
        this.address=address;

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
        return "fname=" + fname + ",lname= " + lname+ " ,address= "+address.toString();
    }


}
