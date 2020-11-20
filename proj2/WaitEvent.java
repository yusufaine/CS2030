package cs2030.simulator;

public class WaitEvent extends Event {

    WaitEvent(Customer customer, double eventTime, int linkedServerID) {
        super(customer, eventTime, linkedServerID, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();

            oldServer.addToQueue(customer);

            Server updatedServer = new Server(oldServer.getID(),
                                              oldServer.isAvailable(),
                                              true,
                                              oldServer.getAvailableTime(),
                                              oldServer.getMaxQueue());

            updatedServer.copyQueue(oldServer);


            ServeEvent newSE = new ServeEvent(customer,
                                              updatedServer.getAvailableTime(),
                                              linkedServerID);

            return Pair.of(shop.replace(updatedServer), newSE);
        });
    }

    public String toString() {
        
        return String.format("%.3f %d waits to be served by server %d", 
                             super.getEventTime(), 
                             super.getCustomerID(), 
                             super.getLinkedServerID());
    }
}