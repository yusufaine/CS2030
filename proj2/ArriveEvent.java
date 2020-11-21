package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    private final boolean isGreedy;

    ArriveEvent(Customer customer) {
        super(customer, customer.getArrivalTime(), shop -> {

            Optional<Server> availableServer = shop.find(y -> y.isAvailable());

            if (availableServer.isPresent()) {
                Server linkedServer = availableServer.get();

                ServeEvent newSE = new ServeEvent(customer, 
                                                  customer.getArrivalTime(), 
                                                  linkedServer);

                return Pair.of(shop, newSE);
            } 

            if (customer.isGreedy()) {
                Server greedyServer = shop.getShortestQueue();
                
                if (greedyServer.getQueueSize() < greedyServer.getMaxQueue()) {
                    WaitEvent newWE = new WaitEvent(customer,
                                                    customer.getArrivalTime(),
                                                    greedyServer);
                    return Pair.of(shop, newWE);
                }
            }
            

            Optional<Server> busyServer = shop.find(y -> (y.getQueue().size() > 0) 
                                                    && (y.getQueue().size() < 
                                                    y.getMaxQueue()));

            if (busyServer.isPresent()) {
                Server linkedServer = busyServer.get();

                WaitEvent newWE = new WaitEvent(customer,
                                                customer.getArrivalTime(),
                                                linkedServer);

                return Pair.of(shop, newWE);
            }

            Optional<Server> singleServer = shop.find(y -> !y.hasWaiting());

            if (singleServer.isPresent()) {
                Server linkedServer = singleServer.get();

                WaitEvent newWE = new WaitEvent(customer,
                                                customer.getArrivalTime(),
                                                linkedServer);

                return Pair.of(shop,newWE);
            }


            LeaveEvent newLE = new LeaveEvent(customer, customer.getArrivalTime());

            return Pair.of(shop, newLE);
        });
        this.isGreedy = customer.isGreedy();
    }

    /**
     * Returns a string representation of the object based on whether the customer
     * is greedy or not.
     *
     * @return     String representation of the object.
     */
    public String toString() {

        if (this.isGreedy) {
            return String.format("%.3f %d(greedy) arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
        } else {
            return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
        }
    }
}