package Multithreading;

public class MyThread extends Thread{
    private int delay;

    public MyThread(String name,int delay){
        super(name);
        this.delay=delay;
    }
    @Override
    public void run() {

    for(int i=1;i<=10;i++){

        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(i+" "+ this.getName());
    }
    }
}
