package examples.callablefuture;

import java.util.concurrent.*;

public class CallableFutureDemo {

    public static void main(String[] args) throws Exception {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Callable<Integer> task =
                () -> 100 + 200;

        Future<Integer> future =
                executor.submit(task);

        System.out.println(future.get());

        executor.shutdown();
    }
}