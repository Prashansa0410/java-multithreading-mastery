package examples.CompletableFuture.ThenApply;

import java.util.concurrent.CompletableFuture;

public class Homework1 {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(()-> "Java")
                .thenApply(String::toUpperCase);
        System.out.println(future.join());

        CompletableFuture<Integer> future2= CompletableFuture
                .supplyAsync(()->10)
                .thenApply(x->x*5)
                .thenApply(x->x+100);

        System.out.println(future2.join());

    }
}
