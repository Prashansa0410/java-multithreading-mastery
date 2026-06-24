package practice.session11_part2.session11;

import java.util.concurrent.*;

/*
* Homework 1

Create:

Callable<Integer>

that calculates:

1 + 2 + 3 + ... + 100

Expected:

5050*/

public class homework1 {
    private static int sum=0;
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Integer> task= ()->
        {
            for(int i=1;i<=100;i++){
                 sum=sum+i;
            }
            return sum;
        };
        Future<Integer> future= service.submit(task);
        int result=future.get();
        System.out.println(result);
        service.shutdown();
    }
}
