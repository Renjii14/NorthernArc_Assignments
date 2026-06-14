package Composition.Wrapper;

public class MainFloat {
    public static void main(String[] args) {
        float value=12f;
        Float j=Float.valueOf(value);
        System.out.println(j);

        Float a=13f;
        System.out.println(a);

        float unboxed=a.floatValue();
        System.out.println(unboxed);

    }
}
