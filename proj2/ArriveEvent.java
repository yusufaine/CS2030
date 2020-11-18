package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    ArriveEvent(Customer customer, int maxQueue) {
        super(customer, 
              customer.getArrivalTime(),
              shop -> {
              
              Optional<Server> availableServer = shop.find(y -> y.isAvailable());

              if (availableServer.isPresent()) {
                  
                  int linkedServerID = availableServer.get().getID();

                  ServeEvent newSE = new ServeEvent(customer,
                                                    customer.getArrivalTime(),
                                                    linkedServerID);

                  return Pair.of(shop, newSE);
              }

              // returns first server that is available and does not have a max q
              Optional<Server> busyServer = shop.find(y -> !y.isAvailable() && 
                                                      y.getQueueSize() != maxQueue);
              if (busyServer.isPresent()) {

                  int linkedServerID       = busyServer.get().getID();
                  Customer waitingCustomer = customer;

                  WaitEvent newWE = new WaitEvent(waitingCustomer,
                                                  waitingCustomer.getArrivalTime(),
                                                  linkedServerID);


                  return Pair.of(shop, newWE);
              }

              LeaveEvent newLE = new LeaveEvent(customer,
                                                customer.getArrivalTime());
              return Pair.of(shop, newLE);
              });
    }

    public String toString() {

        return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
    }
}