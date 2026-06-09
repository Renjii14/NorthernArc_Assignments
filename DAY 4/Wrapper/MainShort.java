package Composition.Wrapper;

public class MainShort {
    public static void main(String[] args) {
        short value=10;
        Short i=Short.valueOf(value);//boxing
        System.out.println(i);

        Short j=20;//auto-boxing
        System.out.println(j);

        short unboxed=j.shortValue();//unboxing
        System.out.println(unboxed);
    }
}
