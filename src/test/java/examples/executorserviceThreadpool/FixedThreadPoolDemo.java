package examples.executorserviceThreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        for(int i=1;i<=5;i++) {
            int finalI = i;
            service.submit(() -> {
                System.out.println("Thread executing task-"+ finalI +"by thread "+Thread.currentThread().getName());
            });
        }
    }
}
