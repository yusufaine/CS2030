package cs2030.simulator;

public class DoneEvent extends Event {

    DoneEvent(Customer customer, double eventTime, int linkedServerID) {
        super(customer, eventTime, linkedServerID, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();
            Customer nextCustomer = oldServer.peekNextCustomer();

            if (oldServer.hasQueue()) {
                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  oldServer.hasWaiting(),
                                                  oldServer.getAvailableTime(),
                                                  oldServer.getMaxQueue());

                updatedServer.copyQueue(oldServer);

                DoneEvent newDE = new DoneEvent(nextCustomer,
                                                updatedServer.getAvailableTime(),
                                                linkedServerID);

                return Pair.of(shop.replace(updatedServer), newDE);
            } else {
                Server updatedServer = new Server(oldServer.getID(),
                                                  true,
                                                  false,
                                                  oldServer.getAvailableTime(),
                                                  oldServer.getMaxQueue());

                updatedServer.copyQueue(oldServer);

                DoneEvent newDE = new DoneEvent(customer, 
                                                updatedServer.getAvailableTime(), 
                                                linkedServerID);

                return Pair.of(shop.replace(updatedServer), newDE);
            }
        });
    }


    public String toString() {
        
        return String.format("%.3f %d done serving by server %d", 
                             super.getEventTime(), 
                             super.getCustomer().getID(), 
                             super.getLinkedServerID());
    }
}