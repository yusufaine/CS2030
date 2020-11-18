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
                Customer nextCustomer = oldServer.pollNextCustomer();
                if (oldServer.hasWaitingCustomer()) {
                    Server updatedServer = new Server(oldServer.getID(),
                                                      false,
                                                      false,
                                                      oldServer.getAvailableTime(),
                                                      nextCustomer);

                    // System.out.println("Old customer impl: " + oldServer
                    //                    .getWaitingCustomer()
                    //                    .getID());
                    // System.out.println("New customer impl: " + nextCustomer.getID());

                    System.out.println("@DoneEvent");                
                    System.out.println("OLD QUEUE " + oldServer.getQueue());
                    System.out.println("NEW QUEUE " + updatedServer.getQueue());

                    DoneEvent newDE = new DoneEvent(customer, 
                                                    updatedServer.getAvailableTime(),
                                                    linkedServerID);

                    return Pair.of(shop.replace(updatedServer), newDE);
                } else {
                    Server updatedServer = new Server(oldServer.getID());

                    DoneEvent newDE = new DoneEvent(customer, 
                                                    updatedServer.getAvailableTime(),
                                                    linkedServerID);

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