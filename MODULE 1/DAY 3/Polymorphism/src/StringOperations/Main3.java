package StringOperations;

public class Main3 {
    public static void main(String[] args) {
        String s="Hello World";
        System.out.println(s.charAt(0));
        System.out.println(s.length());
        System.out.println(s.length()-1);
        System.out.println(s.substring(0,5));
        System.out.println(s.substring(6));
        System.out.println(s.trim());
        System.out.println(s.toLowerCase());
        System.out.println(s.toUpperCase());
        System.out.println(s.indexOf("o"));
        System.out.println(s.lastIndexOf("o"));
        System.out.println(s.contains("z"));
        System.out.println(s.split(" "));
        System.out.println(s.startsWith("Hell"));
        System.out.println(s.endsWith(("ld")));
        System.out.println(s.concat("Computer"));
        String s1="Sachin";
        String s2="Saurav";
        System.out.println(s2.compareTo(s1));
        System.out.println(s2.compareToIgnoreCase(s1));




    }
}
