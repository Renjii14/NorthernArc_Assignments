package String;

public class SbMain {
    public static void main(String[] args) {
//        StringBuilder sb=new StringBuilder("Hello");
        StringBuffer sb=new StringBuffer("Hello");
        sb.append("World");
        System.out.println(sb);
        sb.insert(0,"Hi");
        System.out.println(sb);
        sb.delete(0,2);
        System.out.println(sb);
        sb.replace(7,9,"Java");
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);
    }


}
