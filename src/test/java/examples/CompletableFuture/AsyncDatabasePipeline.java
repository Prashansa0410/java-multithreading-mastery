package examples.CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncDatabasePipeline {

    // A scheduler to simulate network latency asynchronously without blocking threads
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Main thread starts execution...");

        // 1. Trigger the async DB call pipeline
        CompletableFuture<Void> pipeline = fetchUserAgeFromDb("user_id_99")
                .thenApply(ageString -> {
                    // Stage 2: Transform the raw DB string into an Integer
                    System.out.println("[" + Thread.currentThread().getName() + "] thenApply: Converting String to Integer.");
                    return Integer.parseInt(ageString);
                })
                .thenAccept(ageInt -> {
                    // Stage 3: Consume the final result
                    System.out.println("[" + Thread.currentThread().getName() + "] thenAccept: Final processed age is " + ageInt);
                });

        // 2. Proof that Main Thread is completely free and unblocked
        System.out.println("[" + Thread.currentThread().getName() + "] Main thread is FREE! Doing other important work...");

        for (int i = 1; i <= 3; i++) {
            Thread.sleep(800); // Simulate main thread handling other incoming user requests
            System.out.println("[" + Thread.currentThread().getName() + "] Main thread handling request #" + i);
        }

        // Keep the application alive just long enough to let the background async events complete
        pipeline.join();
        scheduler.shutdown();
    }

    /**
     * Simulates a truly non-blocking database network call.
     */
    public static CompletableFuture<String> fetchUserAgeFromDb(String userId) {
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        System.out.println("[" + Thread.currentThread().getName() + "] fetchUserAgeFromDb: Firing async network packet to DB...");

        // Simulate network delay. After 2 seconds, the DB responds.
        scheduler.schedule(() -> {
            System.out.println("[" + Thread.currentThread().getName() + "] DB Hardware responded! Completing the future.");
            futureResult.complete("36"); // This event wakes up the pipeline with the data
        }, 2, TimeUnit.SECONDS);

        // Instantly returns an uncompleted "promise" object. No thread waits here!
        return futureResult;
    }
}