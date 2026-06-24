package examples.callablefuture;

import java.util.concurrent.*;

public class FuturePollingDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<String> task=()->{
            Thread.sleep(3000);
            return "Processing Complete";
        };

        Future<String> result = service.submit(task);
        while(!result.isDone()){
            System.out.println("Processing");
            Thread.sleep(1000);
        }
        System.out.println(result.get());
        service.shutdown();

    }
}
