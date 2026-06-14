package upcast;

public class Student extends Person {

    private String grade;
    private String school;


    public String getgrade(){
        return grade;
    }
    public String school(){
        return school;
    }

    public Student(String fname,String lname,int age,String grade,String school){
        super(fname,lname,age);
        this.grade=grade;
        this.school=school;
    }

    public void study(){
        System.out.println("I am studying in the school "+school+" in the grade: "+grade);
    }

}
