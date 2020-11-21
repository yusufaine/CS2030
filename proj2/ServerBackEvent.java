package cs2030.simulator;

public class ServerBackEvent extends Event {

    ServerBackEvent(Customer customer, double eventTime, Server linkedServer) {
        super(customer, eventTime, linkedServer, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServer.getID()).get();
            Server restedServer = new Server(oldServer.getID(),
                                             true,
                                             oldServer.hasQueue(),
                                             oldServer.getAvailableTime(),
                                             oldServer.getMaxQueue(),
                                             oldServer.isHuman());

            restedServer.copyQueue(oldServer);

            ServerBackEvent newSBE = new ServerBackEvent(
                                         restedServer.peekNextCustomer(),
                                         restedServer.getAvailableTime(),
                                         restedServer);

            return Pair.of(shop.replace(restedServer), newSBE);
        });
    }

    /**
     * Returns a string representation of the object based on whether the customer
     * is greedy or not.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        
        return String.format("%.3f [Server %d] returns from his break", 
                             super.getEventTime(), 
                             super.getLinkedServer().getID());
    }
}