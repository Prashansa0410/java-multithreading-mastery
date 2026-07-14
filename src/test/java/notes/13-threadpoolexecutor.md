# Session 13 – ThreadPoolExecutor Deep Dive

## Why ThreadPoolExecutor Exists

Creating a new thread for every incoming task does not scale.

```java
for(int i=0;i<10000;i++){
    new Thread(task).start();
}
```

Problems:

* High memory consumption
* OS thread creation overhead
* Context switching cost
* Poor throughput under heavy load
* Difficult lifecycle management

Modern backend systems solve this by maintaining a reusable pool of worker threads.

---

# From ExecutorService to ThreadPoolExecutor

When we write:

```java
ExecutorService executor =
        Executors.newFixedThreadPool(3);
```

Java internally creates a ThreadPoolExecutor.

ThreadPoolExecutor is the real engine responsible for:

* Creating worker threads
* Reusing worker threads
* Managing task queues
* Scaling thread count
* Rejecting excess work
* Removing idle threads

Understanding ThreadPoolExecutor is essential for building high-throughput systems.

---

# Core Architecture

Think of ThreadPoolExecutor as a manager.

Tasks arrive continuously.

The manager decides:

* Should I create a thread?
* Should I queue the task?
* Should I create an additional thread?
* Should I reject the task?

Architecture:

```text
                 Task Arrives
                       |
                       v
             +------------------+
             | ThreadPoolExecutor|
             +------------------+
                       |
        +--------------+--------------+
        |                             |
        v                             v
   Worker Threads                Task Queue
```

Worker threads execute tasks.

If workers are busy, tasks wait in the queue.

---

# Core Parameters

## 1. corePoolSize

Minimum number of worker threads maintained by the pool.

Example:

```java
corePoolSize = 2;
```

When the first two tasks arrive:

```text
Worker1 -> Task1
Worker2 -> Task2
```

The pool immediately creates two workers.

Think of corePoolSize as:

```text
Minimum workforce maintained by the system
```

---

## 2. workQueue

Stores waiting tasks.

Example:

```java
new ArrayBlockingQueue<>(2)
```

Queue capacity:

```text
Task3
Task4
```

Tasks wait here until a worker thread becomes available.

---

## 3. maximumPoolSize

Maximum number of worker threads allowed.

Extra threads are created only when:

1. All core threads are busy
2. Queue becomes full

This is one of the most frequently misunderstood concepts in Java concurrency.

---

## 4. keepAliveTime

Controls the lifetime of extra worker threads.

Example:

```java
keepAliveTime = 30 seconds
```

Scenario:

```java
corePoolSize = 2
maximumPoolSize = 5
```

Traffic spike creates:

```text
Worker1
Worker2
Worker3
Worker4
Worker5
```

Traffic drops.

After 30 seconds of inactivity:

```text
Worker3 removed
Worker4 removed
Worker5 removed
```

Pool returns to:

```text
Worker1
Worker2
```

Only non-core threads are removed.

---

# ThreadPoolExecutor Decision Tree

This is the most important concept of the entire session.

When a task arrives:

```text
Current Threads < corePoolSize ?
```

YES

```text
Create New Worker Thread
```

NO

```text
Queue Has Space ?
```

YES

```text
Place Task Into Queue
```

NO

```text
Current Threads < maximumPoolSize ?
```

YES

```text
Create Additional Worker Thread
```

NO

```text
Reject Task
```

This decision tree explains all ThreadPoolExecutor behavior.

---

# Walkthrough Example

Configuration:

```java
corePoolSize = 2
maximumPoolSize = 4
queueSize = 2
```

Tasks arrive:

```text
Task1
Task2
Task3
Task4
Task5
```

---

### Task1

Current Threads:

```text
0 < 2
```

Create:

```text
Worker1 -> Task1
```

---

### Task2

Current Threads:

```text
1 < 2
```

Create:

```text
Worker2 -> Task2
```

---

### Task3

Core pool already satisfied.

Queue has space.

```text
Queue:
Task3
```

---

### Task4

Queue still has space.

```text
Queue:
Task3
Task4
```

Queue becomes full.

---

### Task5

Queue full.

Current Threads:

```text
2 < maximumPoolSize(4)
```

Create:

```text
Worker3 -> Task5
```

Important:

Worker3 executes Task5.

It does NOT execute Task3.

Task3 remains in queue.

This is a common interview trap.

---

# Rejection Policies

When:

```text
Queue Full
AND
Maximum Threads Reached
```

ThreadPoolExecutor invokes a RejectedExecutionHandler.

---

## AbortPolicy (Default)

Behavior:

```java
RejectedExecutionException
```

Use Cases:

* Payments
* Banking
* Order Processing

Never silently lose critical business operations.

---

## CallerRunsPolicy

Instead of rejecting:

```text
Submitting thread executes task
```

Example:

```text
Main Thread
      |
Executes Task
```

Benefit:

```text
Natural Backpressure
```

Incoming traffic automatically slows down.

---

## DiscardPolicy

Behavior:

```text
Silently drop task
```

No exception.

No warning.

Use Cases:

* Metrics
* Telemetry
* Monitoring data

Only when data loss is acceptable.

---

## DiscardOldestPolicy

Behavior:

```text
Remove oldest queued task
Insert newest task
```

Example:

Queue:

```text
Task10
Task11
Task12
```

Task13 arrives.

Result:

```text
Task10 removed
Task13 inserted
```

Use Cases:

* GPS updates
* Live stock prices
* Realtime dashboards

Newest information is more valuable than older information.

---

# Real-World Example: Uber Ride Requests

Configuration:

```java
corePoolSize = 50
maximumPoolSize = 100
queueSize = 1000
```

Scenario:

```text
5000 ride requests arrive simultaneously
```

Behavior:

1. First 50 requests get worker threads
2. Next 1000 requests enter queue
3. Pool expands up to 100 workers
4. Additional requests are rejected

This prevents server overload and protects system stability.

---

# Real-World Example: Flipkart Big Billion Day

Question:

Why not set:

```java
maximumPoolSize = 100000
```

Answer:

Too many threads cause:

* Memory pressure
* Excessive context switching
* CPU thrashing
* Reduced throughput

More threads do not always mean better performance.

---

# Real-World Example: Payment System

Queue becomes full.

Should we use:

```java
DiscardPolicy
```

No.

Payments must never be silently lost.

Preferred policy:

```java
AbortPolicy
```

Fail fast and notify upstream systems.

---

# Real-World Example: Driver GPS Updates

Driver locations arrive every second.

Queue becomes full.

Should we keep old updates?

No.

Old location data becomes stale quickly.

Preferred policy:

```java
DiscardOldestPolicy
```

Keep the newest location information.

---

# Common Interview Questions

### When does maximumPoolSize matter?

Only after the queue becomes full.

---

### Why is maximumPoolSize not used immediately?

ThreadPoolExecutor prefers queueing tasks before creating extra threads.

---

### Why not create unlimited threads?

Because excessive threads increase memory usage and context-switching overhead.

---

### Which rejection policy should a payment system use?

AbortPolicy.

---

### Which rejection policy is suitable for GPS updates?

DiscardOldestPolicy.

---

### What does keepAliveTime control?

The idle lifetime of non-core worker threads.

---

# Key Takeaways

* ThreadPoolExecutor is the foundation of Java thread pools.
* Tasks are queued before extra threads are created.
* maximumPoolSize matters only after queue saturation.
* keepAliveTime removes temporary worker threads.
* Rejection policies determine overload behavior.
* CallerRunsPolicy introduces natural backpressure.
* Understanding ThreadPoolExecutor is critical for designing scalable backend systems.

Mental Model:

```text
Tasks
   ↓
Core Threads
   ↓
Queue
   ↓
Extra Threads
   ↓
Rejection Policy
```

This model forms the basis of high-throughput systems such as payment processors, ride-hailing platforms, trading systems, and the Financial Order Matching Engine that we will build later in this roadmap.
