public class Employee{
    private String name;
    private String position;
    private int salary;

    public String getname(){
        return name;
    }

    public String getposition(){
        return position;
    }
    public int getsalary(){
        return salary;
    }

    public Employee(String name,String position,int salary){
        this.name=name;
        this.position=position;
        this.salary=salary;
    }

    public void work(){
        System.out.println("I am working in a fintech company as "+position);
    }

    public void getDetails(){
        System.out.println("NAME: "+name+"\nPOSITION: "+position+"\nSALARY: "+salary);
    }
}