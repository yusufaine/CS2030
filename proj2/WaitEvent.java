package cs2030.simulator;

public class WaitEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    WaitEvent(Customer customer, double eventTime, int linkedServerID){
        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer     = shop.getServerList().get(linkedServerID - 1);
                Server updatedServer = new Server(oldServer.getID(),
                                                  oldServer.getAvailability(),
                                                  oldServer.hasWaitingCustomer(),
                                                  oldServer.getAvailableTime(),
                                                  customer);
                
                ServeEvent newSE = new ServeEvent(customer,
                                                  customer.getArrivalTime(),
                                                  linkedServerID);
                return Pair.of(shop.replace(updatedServer), newSE);
              });

        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
    }

    public String toString() {
        
        return String.format("%.3f %d waits to be served by server %d", 
                             super.getEventTime(), 
                             this.customer.getID(), 
                             this.linkedServerID);
    }
}