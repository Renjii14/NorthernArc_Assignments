package deadlock;

public class RaceAround {

    public static void main(String[] args) throws InterruptedException {

        class Philosopher implements Runnable {
            private static Object chopstick1 = new Object();
            private static Object chopstick2 = new Object();
            private String name;

            public Philosopher(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                System.out.println(this.name + " is occupying chopstick 1");
                synchronized (chopstick1) {
                    System.out.println(this.name + " occupied chopstick 1 ,going for chopstick 2");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    synchronized (chopstick2) {
                        System.out.println(this.name + "occupied chopstick2 and is now eating...");
                    }
                }

            }
        }
        Thread philosopher1 = new Thread(new Philosopher("Philosopher 1"));
        Thread philosopher2 = new Thread(new Philosopher("Philosopher 2"));
        philosopher1.start();
        philosopher2.start();
    }
}



