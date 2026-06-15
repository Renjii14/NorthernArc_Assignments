package Callable;

import java.util.concurrent.*;

public class Main {
        public static void main(String[] args) {
            //Asynchronous programming
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Integer> futureVal=executor.submit(new MyCallable());
            System.out.println("Future Value will be printed now...");
            try {
                int value=futureVal.get(15, TimeUnit.SECONDS);
                System.out.println("Value:"+value);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception: "+e.getMessage());;
            } catch (ExecutionException e) {
                System.out.println("Execution Exception: "+e.getMessage());;
            } catch (TimeoutException e) {
                System.out.println("Timeout Exception: "+e.getMessage());;
            }
            executor.shutdown();
        }
    }


