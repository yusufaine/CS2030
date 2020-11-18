package cs2030.simulator;

public class ServeEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    ServeEvent(Customer customer, double eventTime, int linkedServerID) {

        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer   = shop.find(x -> x.getID == linkedServerID).get();
                double servingTime = customer.getServiceTime().get();
                double nextAvailableTime = eventTime + servingTime;
                Server updatedServer     = new Server(oldServer.getID(),
                                                      false,
                                                      false,
                                                      nextAvailableTime,
                                                      oldServer.peekNextCustomer());

                

                updatedServer.copyQueue(oldServer.getQueue());
                // updatedServer.addToQueue(waitingCustomer);

                // if (!oldServer.getQueue().isEmpty() && 
                //     !updatedServer.getQueue().isEmpty()){
                    
                //     System.out.println("@ServeEvent");                
                //     System.out.println("OLD QUEUE " + oldServer.getQueue());
                //     System.out.println("NEW QUEUE " + updatedServer.getQueue());
                //     System.out.println("Now serving: " + customer.getID());
                // }
                // System.out.println("Now serving customer: " + customer.getID());

                DoneEvent newDE = new DoneEvent(customer,
                                                nextAvailableTime,
                                                linkedServerID);
                
                return Pair.of(shop.replace(updatedServer), newDE);
              });
        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
    }

    public String toString() {

        return String.format("%.3f %d served by server %d", 
                             super.getEventTime(),
                             this.customer.getID(),
                             linkedServerID);
    }
}