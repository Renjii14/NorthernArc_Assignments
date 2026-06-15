import java.util.Arrays;

public class Arrayintro {
    public static void main(String[] args) {
        System.out.printf("int arrays...");
        int[] intarr=new int[5]; //initially all will be assigned to zero
        for(int i=0;i<=4;i++){
            intarr[i]=i; // assigning values to array
        }
        for(int i=0;i<=4;i++){
            System.out.println(intarr[i]);
        }

        byte[] bytearr=new byte[5];
        for(int i=0;i<=4;i++){
            bytearr[i]=(byte) i;
        }
        for(int i=0;i<=4;i++){
            System.out.println(bytearr[i]);
        }

        short[] shortarr=new short[5];
        for(int i=0;i<=4;i++){
            shortarr[i]=(short) i;
        }
        for(int i=0;i<=4;i++){
            System.out.println(shortarr[i]);
        }

        long[] longarr=new long[5];
        for(int i=0;i<=4;i++){
            longarr[i]=i;
        }
        for(int i=0;i<=4;i++){
            System.out.println(longarr[i]);
        }

        float[] floatarr=new float[5];
        for(int i=0;i<=4;i++){
            floatarr[i]=i;
        }
        for(int i=0;i<=4;i++){
            System.out.println(floatarr[i]);
        }

        double[] doublearr=new double[5];
        for(int i=0;i<=4;i++){
            doublearr[i]=i;
        }
        for(int i=0;i<=4;i++){
            System.out.println(doublearr[i]);
        }

        //char array
        char[] chararr={'a','b','c'};
        for(char c:chararr){
            System.out.println(c);
        }

        //string array
        String[] strarr={"Renji","Lav","Yuva"};
        for(String s:strarr){
            System.out.println(s);
        }
        //Integer array
        Integer[] iarr=new Integer[6];
        for(int i=0;i<=5;i++){
            iarr[i]=i;
        }
        for(int i=0;i<=5;i++){
            System.out.println(iarr[i]);
        }

        //length printing
        System.out.println(intarr.length);
        System.out.println(bytearr.length);
        System.out.println(shortarr.length);
        System.out.println(longarr.length);
        System.out.println(iarr.length);

        //        Sorting
        for(String i:strarr)
            System.out.print(i+" ");
        Arrays.sort(strarr);

        int[] arr={3,4,2,5};
        Arrays.sort(arr);
        for(int val:arr){
            System.out.print(val+" ");
        }

//        swap 2 values in an array
        System.out.println("Swapping 2 values");
        for(int val:intarr){
            System.out.print(val+" ");
        }

        int temp=intarr[1];
        intarr[1]=intarr[intarr.length-1];
        intarr[intarr.length-1]=temp;
        System.out.println("\nswapped");
        for(int val:intarr){
            System.out.print(val+" ");
        }

//        reverse of an array
        System.out.println("\nReversing array\n");
        for(int val:intarr){
            System.out.print(val+" ");
        }
        int len= intarr.length;
        for(int i=0;i<len/2;i++){
            int tem=intarr[i];
            intarr[i]=intarr[len-1-i];
            intarr[len-1]=tem;
        }
        System.out.println("\nReversed array\n");
        for(int val:intarr){
            System.out.print(val+" ");
        }

//        Wrapper class sorting
        Integer[] objint={1,2,42,44,2};
        System.out.println(Arrays.toString(objint));
        Arrays.sort(objint);
        System.out.println(Arrays.toString(objint));





    }
}
