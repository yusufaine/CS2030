package cs2030.simulator;

public class ServerRestEvent extends Event {

    ServerRestEvent(Customer customer, double time, int serverID, double restTime) {
        super(customer, time, serverID, shop -> {
            Server oldServer = shop.find(x -> x.getID() == serverID).get();
            Server restingServer = new Server(oldServer.getID(),
                                              false,
                                              oldServer.hasQueue(),
                                              time + restTime,
                                              oldServer.getMaxQueue());

            restingServer.copyQueue(oldServer);

            ServerBackEvent newSBE = new ServerBackEvent(
                                         customer,
                                         restingServer.getAvailableTime(),
                                         serverID);

            return Pair.of(shop.replace(restingServer), newSBE);
        });
    }

    public String toString() {
        
        return String.format("%.3f [Server %d] goes for a break", 
                             super.getEventTime(), 
                             super.getLinkedServerID());
    }
}