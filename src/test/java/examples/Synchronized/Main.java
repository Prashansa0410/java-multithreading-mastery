package examples.Synchronized;

public class Main {
    public static void main(String[] args) {
        SharedResource sharedResource= new SharedResource();
        System.out.println("Main method started");
        Thread producerThread = new Thread(new ProduceTask(sharedResource));
        Thread consumerThread = new Thread(new ConsumeTask(sharedResource));

        producerThread.start();
        consumerThread.start();

        System.out.println("main method end");


    }
}
