package ObjectOperations;

public class Person {
    private String fname;
    private String lname;
    private int age;

    public String getfname() {
        return fname;
    }

    public String getlname() {
        return lname;
    }

    public int getage() {
        return age;
    }


    public Person(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }

    public void eat() {
        System.out.println(this.fname + " " + this.lname + " is eating");
    }

    public void age() {
        System.out.println(this.fname + " " + this.lname + " age is" + age);
    }

    public void walk() {
        System.out.println(this.fname + " " + "is walking");
    }

    public void talk() {
        System.out.println(this.fname + " " + "is talking");
    }
    @Override
    public boolean equals(Object o) {
            Person p1 = (Person) o;
            return this.fname == p1.fname && this.lname == p1.lname;
        }
        @Override
        public int hashCode() {
            return fname.hashCode() + lname.hashCode();
        }
        @Override
        public String toString() {
            return "fname=" + fname + ",lname" + lname + " ,age" + age;
        }

}

