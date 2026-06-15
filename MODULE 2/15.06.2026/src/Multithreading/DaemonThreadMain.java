package Multithreading;
public class DaemonThreadMain {

        public static void main(String[] args) {

            Thread t1 = new MyThread("Renjitha",1000);
            Thread t2 = new MyThread("Lavanya",500);
            t1.start();
            t2.start();
            t1.setDaemon(true); // process won't wait fort1 thread to finish its execution.
//            if both daemon and join executed join will get high priority
//            daemon threads are infinite loops and background service provider.
//            try {
//                t1.join();//throws checked exception --- so use try catch block or throws
//
//            }
//            catch(InterruptedException e){
//                System.out.println("Error: "+e.getMessage());
//            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(i + " " + Thread.currentThread().getName());
            }


        }
    }



