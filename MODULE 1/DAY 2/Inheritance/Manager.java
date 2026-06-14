public class Manager extends Employee{
    String team;
    public Manager(String fname,String lname,int age,int empId,String dept,String team){
        super(fname,lname,age,empId,dept);
        this.team=team;
    }

    public String getteam(){
        return team;
    }
    public void setteam(){
        this.team=team;
    }

    public void manage(){
        System.out.println(fname+" is managing the "+team);
    }
    
}