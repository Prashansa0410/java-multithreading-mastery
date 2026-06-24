package practice.session11_part2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Homework3 {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Integer> task = ()->{
            throw new ArithmeticException();
        };
        Future<Integer>result=service.submit(task);

        try{
             result.get();
        }
        catch(Exception e){
            e.getCause();
        }
        service.shutdown();

    }
}
