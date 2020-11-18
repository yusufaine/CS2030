package cs2030.simulator;

public class WaitEvent extends Event {

    private final Customer waitingCustomer;
    private final double   eventTime;
    private final int linkedServerID;

    WaitEvent(Customer waitingCustomer, double eventTime, int linkedServerID){
        
        super(waitingCustomer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer     = shop.find(x -> x.getID() == linkedServerID).get();

                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  true,
                                                  oldServer.getAvailableTime(),
                                                  waitingCustomer);

                updatedServer.copyQueue(oldServer.getQueue());
                updatedServer.addToQueue(waitingCustomer);

                System.out.println("@WaitEvent");                
                System.out.println("OLD QUEUE " + oldServer.getQueue());
                System.out.println("NEW QUEUE " + updatedServer.getQueue());
                
                // System.out.println("Next customer: " + updatedServer.pollNextCustomer().getID());
                // System.out.println(" Current impl: " + waitingCustomer.getID());

                ServeEvent newSE = new ServeEvent(waitingCustomer,
                                                  oldServer.getAvailableTime(),
                                                  linkedServerID);

                return Pair.of(shop.replace(updatedServer), newSE);
              });

        this.waitingCustomer = waitingCustomer;
        this.eventTime       = eventTime;
        this.linkedServerID  = linkedServerID;
    }

    public String toString() {
        
        return String.format("%.3f %d waits to be served by server %d", 
                             this.eventTime, 
                             this.waitingCustomer.getID(), 
                             this.linkedServerID);
    }
}