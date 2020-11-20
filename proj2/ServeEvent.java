package cs2030.simulator;

public class ServeEvent extends Event {

    ServeEvent(Customer customer, double eventTime, int linkedServerID) {
        super(customer, eventTime, linkedServerID, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();
            double servingTime = customer.getServiceTime();
            double nextAvailableTime = eventTime + servingTime;

            if (!oldServer.getQueue().isEmpty()) {
                oldServer.pollNextCustomer();
            }
            
            Server updatedServer = new Server(oldServer.getID(),
                                              false,
                                              oldServer.hasQueue(),
                                              nextAvailableTime,
                                              oldServer.getMaxQueue());

            updatedServer.copyQueue(oldServer);

            DoneEvent newDE = new DoneEvent(customer, 
                                            nextAvailableTime,
                                            linkedServerID);

            return Pair.of(shop.replace(updatedServer), newDE);
        });
    }

    public String toString() {

        return String.format("%.3f %d served by server %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServerID());
    }
}