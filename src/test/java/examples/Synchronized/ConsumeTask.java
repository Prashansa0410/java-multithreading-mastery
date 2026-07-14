package examples.Synchronized;

public class ConsumeTask implements Runnable{
    SharedResource sharedResource;
    public ConsumeTask(SharedResource sharedResource){
        this.sharedResource=sharedResource;
    }
    @Override
    public void run() {
        System.out.println();
        try {
            Thread.sleep(1000l);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedResource.consumeItem();
    }
}
