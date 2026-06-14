package Composition.Wrapper;

public class MainByte {
    public static void main(String[] args) {
        byte value=10;
        Byte i=Byte.valueOf(value);//boxing
        System.out.println(i);

        Byte j=20;//auto-boxing
        System.out.println(j);

        int unboxed=j.byteValue();//unboxing
        System.out.println(unboxed);
    }
}
