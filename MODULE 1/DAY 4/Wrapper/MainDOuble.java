package Composition.Wrapper;

public class MainDOuble {
    public static void main(String[] args) {
        double value=120d;
        Double j=Double.valueOf(value);
        System.out.println(j);

        Double a=130d;
        System.out.println(a);

        double unboxed=a.doubleValue();
        System.out.println(unboxed);

    }
}
