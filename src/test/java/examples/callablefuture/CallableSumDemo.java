package examples.callablefuture;

import java.util.concurrent.*;

public class CallableSumDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Callable<Long> task = ()->{
            long sum=0;
            for(int i=1;i<=1000000;i++){
                sum=sum+i;
            }
            return sum;
        };
        Future<Long> result = service.submit(task);
        long res=result.get();
        System.out.println("print the result::"+res);
        service.shutdown();
    }
}
