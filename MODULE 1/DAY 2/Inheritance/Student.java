public class Student extends Person{
   
    private String grade;
    private String school;

   
    public String getgrade(){
        return grade;
    }
    public String getschool(){
        return school;
    }

    public void setter(String grade,String school){
       
        this.grade=grade;
        this.school=school;
    }

    public void study(){
        System.out.println("I am studying in the school "+school+" in the grade: "+grade);
    }

    public void getReportCard(){
        System.out.println("Come to school and tell my name "+getfname()+" to get the report card");
    }
}
