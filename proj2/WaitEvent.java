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
                Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();

                oldServer.addToQueue(customer);

                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  true,
                                                  oldServer.getAvailableTime(),
                                                  oldServer.peekNextCustomer());

                updatedServer.copyQueue(oldServer);
                shop.replace(updatedServer);

                ServeEvent newSE = new ServeEvent(customer,
                                                  oldServer.getAvailableTime(),
                                                  linkedServerID);

                return Pair.of(shop.replace(updatedServer), newSE);
              });

        this.customer = customer;
        this.eventTime       = eventTime;
        this.linkedServerID  = linkedServerID;
    }

    public String toString() {
        
        return String.format("%.3f %d waits to be served by server %d", 
                             this.eventTime, 
                             this.customer.getID(), 
                             this.linkedServerID);
    }
}