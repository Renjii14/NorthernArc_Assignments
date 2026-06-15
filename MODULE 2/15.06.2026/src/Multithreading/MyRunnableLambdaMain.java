package Multithreading;

public class MyRunnableLambdaMain {
    public static void main(String[] args) {
        for(int i=0;i<3;i++){
            new Thread(()->{
                for (int j = 1; j <= 10; j++) {
                    System.out.println(j + " " + Thread.currentThread().getName());
                }
            }).start();
        }
    }
}
