package Overloading;

public class Calculator {
    public int add(int a,int b){
        System.out.println("int addition");
        return a+b;
    }
    public byte add(byte a,byte b){
        System.out.println("Byte addition");
        return (byte)(a+b);
    }
    public short add(short a,short b){
        System.out.println("Short addition");
        return (short)(a+b);
    }
    public float add(float a,float b){
        System.out.println("Float addition");
        return a+b;
    }
    public double add(double a,double b){
        System.out.println("Double addition");
        return a+b;
    }

    public int add(int a,int b,int c){
        return a+b+c;
    }
    public int add(int a,int b,int c,int d){
        return a+b+c+d;
    }
    public String add(String a,String b){
        return a+b;
    }

}
