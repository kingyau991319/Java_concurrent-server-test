import java.util.concurrent.*;

public class SemaphoreExample {

    //public volatile boolean exit = false;
    // can use it to ensure the (ThreadSafe extends Thread) is safe for infinite time

    public static void main(String[] args) {

        final int clientCount = 5;
        final int totalRequestCount = 20;

        Semaphore semaphore = new Semaphore(clientCount);
        //ExecutorService executorService = Executors.newCachedThreadPool(); //60 sec shutdown
        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);

        for (int i = 0; i < totalRequestCount; i++) {
            executorService.execute(()->{
                try{// acquire-> release
                    semaphore.acquire();
                    System.out.print(semaphore.availablePermits() + " ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });

        }

        executorService.shutdown();
    }
}