package cs2030.simulator;

public class DoneEvent extends Event {

    private final boolean isHuman;
    private final boolean isGreedy;

    DoneEvent(Customer customer, double eventTime, Server linkedServer) {
        super(customer, eventTime, linkedServer, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServer.getID()).get();
            Customer nextCustomer = oldServer.peekNextCustomer();

            if (oldServer.hasQueue()) {
                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  oldServer.hasWaiting(),
                                                  oldServer.getAvailableTime(),
                                                  oldServer.getMaxQueue(),
                                                  oldServer.isHuman());

                updatedServer.copyQueue(oldServer);

                DoneEvent newDE = new DoneEvent(nextCustomer,
                                                updatedServer.getAvailableTime(),
                                                updatedServer);

                return Pair.of(shop.replace(updatedServer), newDE);
            } else {
                Server updatedServer = new Server(oldServer.getID(),
                                                  true,
                                                  false,
                                                  oldServer.getAvailableTime(),
                                                  oldServer.getMaxQueue(),
                                                  oldServer.isHuman());

                updatedServer.copyQueue(oldServer);

                DoneEvent newDE = new DoneEvent(customer, 
                                                updatedServer.getAvailableTime(), 
                                                updatedServer);

                return Pair.of(shop.replace(updatedServer), newDE);
            }
        });
        this.isHuman = linkedServer.isHuman();
        this.isGreedy = customer.isGreedy();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        
        if (this.isHuman) {
            if (this.isGreedy) {
                return String.format("%.3f %d(greedy) done serving by server %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d done serving by server %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            }
            
        } else {
            if (this.isGreedy) {
                return String.format("%.3f %d(greedy) done serving by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d done serving by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            }
        }
    }
}