package examples.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        System.out.println("Main Thread running--Thread ::"+Thread.currentThread().getName());
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            System.out.println("Worker Thread::"+Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        future.join();

    }
}
