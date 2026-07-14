package examples.CompletableFuture;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CompletableFuture01 {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Java";
                }
        ).thenApply(upper->{
                System.out.println(Thread.currentThread().getName());
                return upper.toUpperCase();
        });
        System.out.println("Main Thread ::"+Thread.currentThread().getName());
        System.out.println(future.join());
    }
}
