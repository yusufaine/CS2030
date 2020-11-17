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
                if (oldServer.hasWaitingCustomer()) {
                    Server updatedServer = new Server(oldServer.getID(),
                                                      false,
                                                      false,
                                                      oldServer.getAvailableTime(),
                                                      oldServer.getWaitingCustomer());

                    shop.removeCustomer(updatedServer, customer);
                    
                    return Pair.of(shop.replace(updatedServer), 
                                   new DoneEvent(customer,
                                                 updatedServer.getAvailableTime(),
                                                 linkedServerID));
                } else {
                    Server updatedServer = new Server(oldServer.getID());

                    shop.removeCustomer(updatedServer, customer);
                                        
                    return Pair.of(shop.replace(updatedServer), 
                                   new DoneEvent(customer,
                                                 updatedServer.getAvailableTime(),
                                                 linkedServerID));
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