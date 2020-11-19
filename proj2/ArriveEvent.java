package cs2030.simulator;

import java.util.Optional;

public class ArriveEvent extends Event {

    ArriveEvent(Customer customer) {
        super(customer, 
              customer.getArrivalTime(),
              shop -> {
              
              Optional<Server> availableServer = shop.find(y -> y.isAvailable());

              if (availableServer.isPresent()) {
                  
                  Server oldServer   = availableServer.get();
                  int linkedServerID = oldServer.getID();

                  ServeEvent newSE = new ServeEvent(customer,
                                                    customer.getArrivalTime(),
                                                    linkedServerID);

                  return Pair.of(shop, newSE);
              }

              Optional<Server> noWaitingServer = shop.find(y -> !y.isAvailable() && 
                                                           !y.hasWaitingCustomer());
              if (noWaitingServer.isPresent()) {
                  Server oldServer     = noWaitingServer.get();
                  int linkedServerID   = oldServer.getID();

                  WaitEvent newWE = new WaitEvent(customer,
                                                  customer.getArrivalTime(),
                                                  linkedServerID);


                  return Pair.of(shop, newWE);
              }

              LeaveEvent newLE = new LeaveEvent(customer,
                                                customer.getArrivalTime());
              return Pair.of(shop, newLE);
              });
    }

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

              Optional<Server> busyServer = shop.find(y -> !y.isAvailable() && 
                                                      y.getQueueSize() != maxQueue);
              if (busyServer.isPresent()) {

                  int linkedServerID       = busyServer.get().getID();
                  Customer waitingCustomer = customer;

                  WaitEvent newWE = new WaitEvent(waitingCustomer,
                                                  waitingCustomer.getArrivalTime(),
                                                  linkedServerID);


                  return Pair.of(shop, newWE);
              } else {
                  LeaveEvent newLE = new LeaveEvent(customer,
                                                    customer.getArrivalTime());
                  return Pair.of(shop, newLE);
              }

              });
    }

    public String toString() {

        return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             super.getCustomer().getID());
    }
}