package Composition.Wrapper;

public class MainBool {
    public static void main(String[] args) {
        boolean value=true;
        Boolean j=Boolean.valueOf(value);
        System.out.println(j);

        Boolean a=true;
        System.out.println(a);

        boolean unboxed=a.booleanValue();
        System.out.println(unboxed);

    }
}
