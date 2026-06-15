package Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for(int i = 1; i <= 10; i++) {
            int taskId = i; // effectively final
            executor.submit(() -> {
                System.out.println(
                        "Task " + taskId +
                                " executed by " +
                                Thread.currentThread().getName()
                );
            });
        }
        executor.shutdown();
    }
}

