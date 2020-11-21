package cs2030.simulator;

public class ServerRestEvent extends Event {

    ServerRestEvent(Customer customer, 
                    double eventTime, 
                    Server linkedServer, 
                    double restTime) {

        super(customer, eventTime, linkedServer, shop -> {
            Server oldServer = shop.find(x -> x.getID() == linkedServer.getID()).get();
            Server restingServer = new Server(oldServer.getID(),
                                              false,
                                              oldServer.hasQueue(),
                                              eventTime + restTime,
                                              oldServer.getMaxQueue(),
                                              oldServer.isHuman());

            restingServer.copyQueue(oldServer);

            ServerBackEvent newSBE = new ServerBackEvent(
                                         customer,
                                         restingServer.getAvailableTime(),
                                         restingServer);

            return Pair.of(shop.replace(restingServer), newSBE);
        });
    }

    /**
     * Returns a string representation of the object based on whether the customer
     * is greedy or not.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        
        return String.format("%.3f [Server %d] goes for a break", 
                             super.getEventTime(), 
                             super.getLinkedServer().getID());
    }
}