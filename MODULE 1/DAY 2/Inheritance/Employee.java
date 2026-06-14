
public class Employee extends Person{
    private int empId;
    private String dept;
    public int getempId(){
        return empId;
    }
    public String getdept(){
        return dept;
    }
    public void setempid(int empId){
        this.empId=empId;
    }
    public void setdept(String dept){
        this.dept=dept;
    }
    public Employee(String fname,String lname,int age,int empId,String dept){
        super(fname, lname, age);
        this.empId=empId;
        this.dept=dept;
    }
    public void work(){
        System.out.println(fname+" is working in "+dept+" department.");
    }
     public void showDetails(){
        //initially to reuse we change private to protected. other way use super keyword
        // System.out.println("NAME: "+fname+" "+lname);
        // System.out.println("AGE: "+age);
        super.showDetails();
        System.out.println("EMP ID: "+empId);
        System.out.println("DEPT: "+ dept);
    }
}