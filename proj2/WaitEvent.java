package cs2030.simulator;

public class WaitEvent extends Event {

    private final boolean isHuman;
    private final boolean isGreedy;

    WaitEvent(Customer customer, double eventTime, Server linkedServer) {
        super(customer, eventTime, linkedServer, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServer.getID()).get();

            oldServer.addToQueue(customer);

            Server updatedServer = new Server(oldServer.getID(),
                                              oldServer.isAvailable(),
                                              true,
                                              oldServer.getAvailableTime(),
                                              oldServer.getMaxQueue(),
                                              oldServer.isHuman());

            updatedServer.copyQueue(oldServer);


            ServeEvent newSE = new ServeEvent(customer,
                                              updatedServer.getAvailableTime(),
                                              updatedServer);

            return Pair.of(shop.replace(updatedServer), newSE);
        });
        this.isHuman = linkedServer.isHuman();
        this.isGreedy = customer.isGreedy();
    }

    /**
     * Returns a string representation of the object based on whether the customer
     * is greedy or not.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        
        if (this.isHuman) {
            if (this.isGreedy) {
                return String.format("%.3f %d(greedy) waits to be served by server %d",
                             super.getEventTime(), 
                             super.getCustomerID(), 
                             super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d waits to be served by server %d", 
                             super.getEventTime(), 
                             super.getCustomerID(), 
                             super.getLinkedServer().getID());
            }
            
        } else {
            if (this.isGreedy) {
                return String.format("%.3f %d(greedy) waits to be served by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomerID(), 
                             super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d waits to be served by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomerID(), 
                             super.getLinkedServer().getID());
            }
        }
    }
}