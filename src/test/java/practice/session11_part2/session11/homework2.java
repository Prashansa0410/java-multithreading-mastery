package practice.session11_part2.session11;

import java.util.concurrent.*;

/**
 * Homework 2
 *
 * Create 3 Callables:
 *
 * Task1 → 100
 * Task2 → 200
 * Task3 → 300
 *
 * Submit all.
 *
 * Print:
 *
 * 600
 */
public class homework2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Integer> task1 = ()->{
          return 100;
        };
        Callable<Integer> task2 = ()->{
            return 200;
        };
        Callable<Integer> task3 = ()->{
            return 300;
        };
       Future<Integer> future1= service.submit(task1);
        Future<Integer> future2= service.submit(task2);
        Future<Integer> future3= service.submit(task3);
       int t1=future1.get();
       int t2=future2.get();
       int t3=future3.get();
       int output=t1+t2+t3;
        System.out.println("output::"+output);
        service.shutdown();
    }
}
