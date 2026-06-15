package ResourceSharing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Thread t1=new MyThread("Sachin",new FileInputStream("C:\\Users\\renjitha.k\\Desktop\\practice\\Example\\src\\MODULE 2\\15.06.2026\\src\\ResourceSharing\\Sachin.txt"));
            Thread t2=new MyThread("Saurav",new FileInputStream("C:\\Users\\renjitha.k\\Desktop\\practice\\Example\\src\\MODULE 2\\15.06.2026\\src\\ResourceSharing\\Saurav.txt"));
            MyThread.openDestinationWriter();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            MyThread.closeDestinationWriter();
            System.out.println("Exiting main thread!!!");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
