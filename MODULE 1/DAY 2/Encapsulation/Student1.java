public class Student1{
    private String name;
    private String grade;
    private String school;

    public String getname(){
        return name;
    }
    public String getgrade(){
        return grade;
    }
    public String school(){
        return school;
    }

    public Student1(String name,String grade,String school){
        this.name=name;
        this.grade=grade;
        this.school=school;
    }

    public void study(){
        System.out.println("I am studying in the school "+school+" in the grade: "+grade);
    }

    public void getReportCard(){
        System.out.println("Come to school and tell my name "+name+" to get the report card");
    }
}