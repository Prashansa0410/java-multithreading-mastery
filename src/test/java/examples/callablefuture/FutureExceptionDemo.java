package examples.callablefuture;

import java.util.concurrent.*;

public class FutureExceptionDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Integer> task = ()->{
            throw new ArithmeticException();
        };
        Future<Integer>result=service.submit(task);

        try{
             result.get();
        }
        catch(ExecutionException e){
            System.out.println(e.getCause());
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();

    }
}
