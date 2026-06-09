package StringOperations;

public class Main2 {
    public static void main(String[] args) {
        String s=new String("Hello");
        String s2="Hello";
        System.out.println(s==s2);
        System.out.println(s.intern()==s2);
    }
}
