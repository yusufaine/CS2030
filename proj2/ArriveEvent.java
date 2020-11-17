package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    private final Customer customer;

    ArriveEvent(Customer customer) {
        super(customer, 
              customer.getArrivalTime(),
              shop -> {
              
              Optional<Server> availableServer = shop.find(y -> y.isAvailable() &&
                                                           !shop.isBusy(y));
              if (availableServer.isPresent()) {
                  
                  Server oldServer     = availableServer.get();
                  int linkedServerID   = oldServer.getID();

                  ServeEvent newSE = new ServeEvent(customer,
                                                    customer.getArrivalTime(),
                                                    linkedServerID);

                  return Pair.of(shop, newSE);
              }


              //if (queue <= maxQueueSize) {}
              Optional<Server> noWaitingServer = shop.find(y -> shop.isBusy(y) &&
                                                           !shop.hasQueue(y));
              if (noWaitingServer.isPresent()) {
                  Server oldServer     = noWaitingServer.get();
                  int linkedServerID   = oldServer.getID();

                  WaitEvent newWE = new WaitEvent(customer,
                                                  customer.getArrivalTime(),
                                                  linkedServerID);

                  return Pair.of(shop, newWE);
              }

              //else{}
              LeaveEvent newLE = new LeaveEvent(customer,
                                                customer.getArrivalTime());
              return Pair.of(shop, newLE);
              });

        this.customer = customer;
    }

    public String toString() {

        return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
    }
}