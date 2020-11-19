package cs2030.simulator;

public class ServeEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    ServeEvent(Customer customer, double eventTime, int linkedServerID) {

        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer   = shop.find(x -> x.getID() == linkedServerID).get();
                double servingTime = customer.getServiceTime();

                System.out.println("@ServeEvent");
                
                if (oldServer.hasQueue() == false) {
                    double nextAvailableTime = eventTime + servingTime;

                    Server updatedServer     = new Server(oldServer.getID(),
                                                          false,
                                                          false,
                                                          nextAvailableTime,
                                                          oldServer.getWaitingCustomer());

                    updatedServer.copyQueue(oldServer.getQueue());

                    // if (updatedServer.hasQueue() && oldServer.hasQueue()) {
                    //     System.out.println("@ServeEvent");
                    //     System.out.println("Now serving: " + customer.getID());
                    //     System.out.println("OLD QUEUE: " + oldServer.getQueue());
                    //     System.out.println("NEW QUEUE: " + updatedServer.getQueue());
                    //     System.out.println();
                    // }
                    System.out.println("No queue detected");
                    System.out.println("Now serving  : " + customer.getID());
                    System.out.println("Next customer: " + "NULL");
                    System.out.println("Queue status : " + updatedServer.hasQueue());
                    System.out.println("Queue size   : " + updatedServer.getQueueSize());
                    System.out.println();

                    DoneEvent newDE = new DoneEvent(customer,
                                                    nextAvailableTime,
                                                    linkedServerID);

                    return Pair.of(shop.replace(updatedServer), newDE);
                } else {
                    double nextAvailableTime = eventTime + servingTime;
                    Server updatedServer     = new Server(oldServer.getID(),
                                                          false,
                                                          false,
                                                          nextAvailableTime,
                                                          oldServer.pollNextCustomer());

                    updatedServer.copyQueue(oldServer.getQueue());

                    System.out.println("Queue detected");
                    System.out.println("Now serving  : " + customer.getID());
                    System.out.println("Next customer: " + updatedServer.getWaitingCustomer().getID());
                    System.out.println("Queue status : " + updatedServer.hasQueue());
                    System.out.println("Queue size   : " + updatedServer.getQueueSize());
                    System.out.println();

                    DoneEvent newDE = new DoneEvent(customer,
                                                    nextAvailableTime,
                                                    linkedServerID);

                    return Pair.of(shop.replace(updatedServer), newDE);

                }
              });
        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
    }

    public String toString() {

        return String.format("%.3f %d served by server %d", 
                             this.eventTime,
                             this.customer.getID(),
                             linkedServerID);
    }
}