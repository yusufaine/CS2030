package cs2030.simulator;

import cs2030.simulator.RNGImpl;

public class ServeEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    ServeEvent(Customer customer, 
               double eventTime, 
               int linkedServerID) {

        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer   = shop.getServerList().get(linkedServerID - 1);
                double servingTime = customer.getServiceTime().get();
                double nextAvailableTime = eventTime + servingTime;
                Server updatedServer     = new Server(oldServer.getID(),
                                                      oldServer.getAvailability(),
                                                      false,
                                                      nextAvailableTime,
                                                      customer);

                shop.addBusyServer(updatedServer, customer);
                
                DoneEvent newDE = new DoneEvent(customer,
                                                nextAvailableTime,
                                                linkedServerID);
                
                return Pair.of(shop.replace(updatedServer), newDE);
              });
        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
        super.addBusyServer(linkedServerID);
    }

    public String toString() {

        return String.format("%.3f %d served by server %d", 
                             super.getEventTime(),
                             this.customer.getID(),
                             linkedServerID);
    }
}