package cs2030.simulator;

public class ServeEvent extends Event {

    private final boolean isHuman;
    private final boolean isGreedy;

    ServeEvent(Customer customer, double eventTime, Server linkedServer) {
        super(customer, eventTime, linkedServer, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServer.getID()).get();
            double servingTime = customer.getServiceTime();
            double nextAvailableTime = eventTime + servingTime;

            if (!oldServer.getQueue().isEmpty()) {
                oldServer.pollNextCustomer();
            }

            Server updatedServer = new Server(oldServer.getID(),
                                              false,
                                              oldServer.hasQueue(),
                                              nextAvailableTime,
                                              oldServer.getMaxQueue(),
                                              oldServer.isHuman());

            updatedServer.copyQueue(oldServer);

            DoneEvent newDE = new DoneEvent(customer, 
                                            nextAvailableTime,
                                            updatedServer);

            return Pair.of(shop.replace(updatedServer), newDE);
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
                return String.format("%.3f %d(greedy) served by server %d", 
                                 super.getEventTime(), 
                                 super.getCustomer().getID(), 
                                 super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d served by server %d", 
                                 super.getEventTime(), 
                                 super.getCustomer().getID(), 
                                 super.getLinkedServer().getID());
            }
        } else {
            if (this.isGreedy) {
                return String.format("%.3f %d(greedy) served by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            } else {
                return String.format("%.3f %d served by self-check %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServer().getID());
            }
        }
    }
}