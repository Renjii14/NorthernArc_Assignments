package Multithreading;

public class ThreadMain {
    public static void main(String[] args) {

            Thread t1 = new MyThread("Renjitha",500);
            Thread t2 = new MyThread("Lavanya",1000);
            t1.start();
            t2.start();
        try {
            t1.join();//throws checked exception --- so use try catch block or throws
            t2.join();
        }
            catch(InterruptedException e){
                System.out.println("Error: "+e.getMessage());
            }
            System.out.println("Active threads: " + Thread.activeCount());
            for (int i = 1; i <= 10; i++) {
                System.out.println(i + " " + Thread.currentThread().getName());
            }


    }
}
