package examples.Synchronized;

public class SharedResource {

    boolean itemAvailable = false;

    //synchronized put the monitor lock
    public synchronized void addItem(){
        itemAvailable =true;
        System.out.println("Item added by::"+Thread.currentThread().getName()+" and invoking all threads");
        notifyAll();
    }

    public synchronized void consumeItem(){
        System.out.println("consumeItem method invoked by::"+Thread.currentThread().getName());

        //using while loop
        while(!itemAvailable){
            try{
                System.out.println("Thread::"+Thread.currentThread().getName()+" is waiting now");
                wait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Item consumed by::"+Thread.currentThread().getName());
        itemAvailable=false;
    }
}
