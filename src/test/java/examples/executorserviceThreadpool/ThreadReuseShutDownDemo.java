package examples.executorserviceThreadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ThreadReuseShutDownDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for(int i=0;i<6;i++){
            int j=i+1;
            service.submit(()->{
                try {
                    Thread.sleep(2000);
                    System.out.println("Task "+j+" executed by thread::"+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
        try {
            service.submit(() ->
            {
                System.out.println("Working");
            });
        }
        catch (RejectedExecutionException e){
            System.out.println("Task Rejected");
        }
    }
}
