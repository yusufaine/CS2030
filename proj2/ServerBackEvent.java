package cs2030.simulator;

public class ServerBackEvent extends Event {

    ServerBackEvent(Customer customer, double eventTime, int linkedServerID) {
        super(customer, eventTime, linkedServerID, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();
            Server restedServer = new Server(oldServer.getID(),
                                             true,
                                             oldServer.hasQueue(),
                                             oldServer.getAvailableTime(),
                                             oldServer.getMaxQueue());

            restedServer.copyQueue(oldServer);

            ServerBackEvent newSBE = new ServerBackEvent(
                                         restedServer.peekNextCustomer(),
                                         restedServer.getAvailableTime(),
                                         linkedServerID);

            return Pair.of(shop.replace(restedServer), newSBE);
        });
    }

    public String toString() {
        
        return String.format("%.3f [Server %d] returns from his break", 
                             super.getEventTime(), 
                             super.getLinkedServerID());
    }
}