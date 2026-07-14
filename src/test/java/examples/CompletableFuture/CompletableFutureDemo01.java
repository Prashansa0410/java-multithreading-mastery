package examples.CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDemo01 {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            System.out.println("Thread exceuting the task::"+Thread.currentThread().getName());
        },service);
        System.out.println("Main::"+Thread.currentThread().getName());
        future.join();
        service.shutdown();
    }
}
