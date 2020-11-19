package cs2030.simulator;

public class DoneEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    DoneEvent(Customer customer, double eventTime, int linkedServerID) {
        super(customer, 
              eventTime,
              linkedServerID,
              shop -> {
                Server oldServer = shop.getServerList().get(linkedServerID - 1);
                if (oldServer.hasQueue()) {
                    Server updatedServer = new Server(oldServer.getID(),
                                                      false,
                                                      false,
                                                      oldServer.getAvailableTime(),
                                                      oldServer.getWaitingCustomer());

                    updatedServer.copyQueue(oldServer.getQueue());

                    DoneEvent newDE = new DoneEvent(customer, 
                                                    updatedServer.getAvailableTime(),
                                                    linkedServerID);

                    return Pair.of(shop.replace(updatedServer), newDE);
                } else {
                    Server updatedServer = new Server(oldServer.getID());

                    DoneEvent newDE = new DoneEvent(customer, 
                                                    updatedServer.getAvailableTime(),
                                                    linkedServerID);
                    
                    updatedServer.copyQueue(oldServer.getQueue());

                    return Pair.of(shop.replace(updatedServer), newDE);
                }
            });
        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
    }


    public String toString() {
        
        return String.format("%.3f %d done serving by server %d", 
                             super.getEventTime(), 
                             this.customer.getID(), 
                             linkedServerID);
    }
}