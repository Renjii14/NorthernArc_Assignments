package Overloading;

public class Main {
    public static void main(String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.add(1, 2));
        c.add(1, 2, 3);
        c.add(1, 2, 3, 4);
        c.add("a","b");
    }
}
