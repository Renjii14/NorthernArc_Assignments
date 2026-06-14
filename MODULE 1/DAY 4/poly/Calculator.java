package Composition.poly;

public class Calculator {
//    public int add(int a,int b){
//        return a+b;
//    }
    public long add(long a,long b){
        System.out.println("Long");
        return a+b;
    }
    public short add(short a,short b){
        System.out.println("Short");
        return (short)(a+b);
    }
    public byte add(byte a,byte b){
        System.out.println("Byte");
        return (byte)(a+b);
    }
    public float add(float a,float b){
        System.out.println("Float");
        return a+b;
    }

    public double add(double a,double b){
        System.out.println("Double");
        return a+b;
    }

    public Integer add(Integer a,Integer b){
        System.out.println("Integer");
        return a+b;
    }
}
