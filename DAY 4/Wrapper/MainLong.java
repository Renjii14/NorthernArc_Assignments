package Composition.Wrapper;

public class MainLong {
    public static void main(String[] args) {
        long value=120l;
        Long j=Long.valueOf(value);
        System.out.println(j);

        Long a=130l;
        System.out.println(a);

        long unboxed=a.longValue();
        System.out.println(unboxed);

    }
}
