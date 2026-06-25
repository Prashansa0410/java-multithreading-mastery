# Session 12 - ExecutorService & Thread Pools

## Why Do We Need Thread Pools?

Creating a thread is an expensive operation.

When we create:

```java
new Thread(task).start();
```

Java creates a corresponding OS thread.

Each thread requires:

* Stack memory
* OS scheduling
* Context switching
* Thread lifecycle management

If thousands of requests arrive and we create a new thread for each request, the system can quickly consume memory and CPU resources.

Example:

```java
for(int i=0;i<10000;i++){
    new Thread(task).start();
}
```

This creates 10,000 threads, which is inefficient and does not scale well.

---

## Solution: Thread Pool

Instead of creating a new thread for every task, we create a fixed set of worker threads and reuse them.

Concept:

```text
Tasks
   ↓
Task Queue
   ↓
Worker Threads
```

Benefits:

* Thread reuse
* Reduced thread creation cost
* Better CPU utilization
* Improved scalability
* Controlled concurrency

---

## What is ExecutorService?

ExecutorService is a framework provided by Java to manage thread pools.

Instead of:

```java
new Thread(task).start();
```

we write:

```java
executor.submit(task);
```

ExecutorService takes care of:

* Thread creation
* Thread reuse
* Task scheduling
* Queue management
* Shutdown management

---

## Creating a Fixed Thread Pool

```java
ExecutorService executor =
        Executors.newFixedThreadPool(3);
```

Meaning:

* Maximum 3 worker threads are created.
* Threads are reused for multiple tasks.
* Additional tasks wait in a queue.

---

## Example

```java
ExecutorService executor =
        Executors.newFixedThreadPool(2);

for(int i=1;i<=5;i++){

    int taskId = i;

    executor.submit(() -> {

        System.out.println(
                "Task " + taskId +
                " executed by " +
                Thread.currentThread().getName()
        );

    });
}

executor.shutdown();
```

Possible Output:

```text
Task 1 executed by pool-1-thread-1
Task 2 executed by pool-1-thread-2
Task 3 executed by pool-1-thread-1
Task 4 executed by pool-1-thread-2
Task 5 executed by pool-1-thread-1
```

Observation:

Only two worker threads are used.

Threads are reused.

---

## Internal Architecture

When submit() is called:

```text
Main Thread
      |
      V
 submit(task)
      |
      V
  Task Queue
      |
      V
 Worker Threads
      |
      V
 Execute Task
```

Important:

Tasks and Threads are different concepts.

Many tasks can be executed using a small number of worker threads.

---

## Task Queue Behavior

Example:

Pool Size:

```java
Executors.newFixedThreadPool(2);
```

Tasks:

```text
Task1
Task2
Task3
Task4
Task5
```

Execution:

```text
Worker1 -> Task1
Worker2 -> Task2
```

Waiting Queue:

```text
Task3
Task4
Task5
```

As workers become free:

```text
Worker1 -> Task3
Worker2 -> Task4
```

and so on.

This queueing behavior is one of the most important concepts in ExecutorService.

---

## submit() vs execute()

### submit()

```java
Future<?> future =
        executor.submit(task);
```

Characteristics:

* Returns Future
* Can retrieve result
* Can retrieve exceptions
* Can check completion status

---

### execute()

```java
executor.execute(task);
```

Characteristics:

* Returns void
* No Future object
* Fire-and-forget execution

---

### Interview Difference

```text
submit() returns Future

execute() returns void
```

Both methods submit work to worker threads.

Neither method executes the task directly.

---

## shutdown()

Used to gracefully stop the ExecutorService.

```java
executor.shutdown();
```

Behavior:

* No new tasks accepted
* Existing tasks continue execution
* Queued tasks continue execution

Example:

```java
executor.shutdown();

executor.submit(task);
```

Output:

```text
RejectedExecutionException
```

because the pool is shutting down.

---

## shutdownNow()

```java
executor.shutdownNow();
```

Behavior:

* Attempts immediate shutdown
* Attempts to interrupt running threads
* Does not guarantee termination

Interview Answer:

```text
shutdown() = Graceful shutdown

shutdownNow() = Immediate shutdown attempt
```

---

## RejectedExecutionException

Occurs when:

```java
executor.shutdown();

executor.submit(task);
```

Reason:

The ExecutorService is no longer accepting new tasks.

---

## Real-World Analogy

Restaurant Example:

Customers:

```text
Tasks
```

Chefs:

```text
Worker Threads
```

Waiting Line:

```text
Task Queue
```

Restaurant Manager:

```text
ExecutorService
```

When all chefs are busy, new customers wait in line until a chef becomes available.

---

## Interview Questions

### Why is new Thread() expensive?

Because each thread requires:

* OS thread creation
* Stack memory
* Scheduling
* Context switching

---

### What problem does a thread pool solve?

Thread pools reuse worker threads and prevent excessive thread creation.

---

### Pool size = 3 and 100 tasks arrive. How many threads are created?

Answer:

```text
3 worker threads
```

Remaining tasks wait in queue.

---

### What happens to tasks when all workers are busy?

Tasks are placed into the executor queue and wait until a worker thread becomes available.

---

### What does shutdown() do?

Answer:

```text
Stops accepting new tasks.

Already running and queued tasks continue execution.
```

---

## Key Takeaways

1. Creating threads is expensive.
2. Thread pools reuse worker threads.
3. ExecutorService manages worker threads and task execution.
4. FixedThreadPool creates a fixed number of reusable worker threads.
5. Tasks are placed in a queue when all workers are busy.
6. submit() returns Future.
7. execute() returns void.
8. shutdown() performs graceful shutdown.
9. shutdownNow() attempts immediate shutdown.
10. Tasks and threads are different concepts.

Mental Model:

```text
Tasks
   ↓
Task Queue
   ↓
Worker Threads
```

This is the foundation for ThreadPoolExecutor, BlockingQueue, and the Order Ingestion Layer of our Financial Order Matching Engine project.
