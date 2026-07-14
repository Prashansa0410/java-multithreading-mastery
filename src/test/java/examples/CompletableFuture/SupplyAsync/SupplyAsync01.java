package examples.CompletableFuture.SupplyAsync;

import java.util.concurrent.CompletableFuture;

public class SupplyAsync01 {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->
        {
            System.out.println(
                    Thread.currentThread().getName());
            return 50+70;
        });

        int val=future.join();
        System.out.println(
                Thread.currentThread().getName());
        System.out.println("print the value::"+val);
    }
}
