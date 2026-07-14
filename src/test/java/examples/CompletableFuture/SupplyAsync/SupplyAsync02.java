package examples.CompletableFuture.SupplyAsync;

import java.util.concurrent.CompletableFuture;

public class SupplyAsync02 {
    public static void main(String[] args) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(()->
        {
            return new User("Prashansa");
        });

        CompletableFuture<String> future01 = CompletableFuture.supplyAsync(()->
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Employee Loaded";
        });

        System.out.println(future.join().name);
        System.out.println("Thread::"+Thread.currentThread().getName()+" doing work");
        System.out.println(future01.join());
    }

}
