package examples.CompletableFuture.ThenApply;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Homework2 {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(()-> {System.out.println("Supplier : " +
                        Thread.currentThread().getName());
               return  "Java";})
                .thenApplyAsync(s->{
                    System.out.println(Thread.currentThread().getName());
                    return s.toUpperCase();
                });
        System.out.println(future.join());
    }
}
