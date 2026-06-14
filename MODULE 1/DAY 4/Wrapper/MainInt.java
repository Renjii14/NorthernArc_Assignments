package Composition.Wrapper;

public class MainInt {
    public static void main(String[] args) {
        int value=10;
        Integer i=Integer.valueOf(value);//boxing
        System.out.println(i);

        Integer j=20;//auto-boxing
        System.out.println(j);

        int unboxed=j.intValue();//unboxing
        System.out.println(unboxed);
    }
}
