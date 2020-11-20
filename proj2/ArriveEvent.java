package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    ArriveEvent(Customer customer) {
        super(customer, customer.getArrivalTime(), shop -> {

            Optional<Server> availableServer = shop.find(y -> y.isAvailable());

            if (availableServer.isPresent()) {
                Server oldServer = availableServer.get();
                int linkedServerID = oldServer.getID();

                ServeEvent newSE = new ServeEvent(customer, 
                                                  customer.getArrivalTime(), 
                                                  linkedServerID);

                return Pair.of(shop, newSE);
            } 

            Optional<Server> busyServer = shop.find(y -> (y.getQueue().size() >= 1) 
                                                    && (y.getQueue().size() < 
                                                    y.getMaxQueue()));

            if (busyServer.isPresent()) {
                Server oldServer = busyServer.get();
                int linkedServerID = oldServer.getID();

                WaitEvent newWE = new WaitEvent(customer,
                                                customer.getArrivalTime(),
                                                linkedServerID);

                return Pair.of(shop, newWE);
            }

            Optional<Server> singleServer = shop.find(y -> !y.hasWaiting());

            if (singleServer.isPresent()) {
                Server oldServer = singleServer.get();
                int linkedServerID = oldServer.getID();

                WaitEvent newWE = new WaitEvent(customer,
                                                customer.getArrivalTime(),
                                                linkedServerID);

                return Pair.of(shop,newWE);
            }


            LeaveEvent newLE = new LeaveEvent(customer, customer.getArrivalTime());

            return Pair.of(shop, newLE);
        });
    }

    public String toString() {

        return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
    }
}