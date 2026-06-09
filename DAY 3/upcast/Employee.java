package upcast;

public class Employee extends Person{

    private String position;
    private int salary;



    public String getposition(){
        return position;
    }
    public int getsalary(){
        return salary;
    }

    public Employee(String fname,String lname,int age,String position,int salary){
        super(fname, lname, age);
        this.position=position;
        this.salary=salary;
    }

    public void work(){
        System.out.println("I am working in a fintech company as "+position);
    }


}
