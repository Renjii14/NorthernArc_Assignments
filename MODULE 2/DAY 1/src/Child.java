import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Child {
    private String fname;
    private String lname;
    private String dob;

    public Child(String fname, String lname, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return ("\n" + String.join("-", this.fname, this.lname, this.dob));
        //return

    }
}
