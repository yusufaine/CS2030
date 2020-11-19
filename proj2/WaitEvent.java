package cs2030.simulator;

public class WaitEvent extends Event {

    private final Customer customer;
    private final double   eventTime;
    private final int linkedServerID;

    WaitEvent(Customer customer, double eventTime, int linkedServerID){
        
        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer = shop.find(x -> x.getID() == linkedServerID).get();

                System.out.println("@WaitEvent"); 
                System.out.println("Server ID: " + oldServer.getID());
                System.out.println(String.format("Available @ %.3f", oldServer.getAvailableTime()));
                System.out.println("Queue Status: " + oldServer.hasQueue());
                System.out.println("OLD QUEUE " + oldServer.getQueue());

                oldServer.addToQueue(customer);



                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  true,
                                                  oldServer.getAvailableTime() + customer.getServiceTime(),
                                                  oldServer.peekNextCustomer());

                updatedServer.copyQueue(oldServer.getQueue());

                System.out.println("NEW QUEUE " + updatedServer.getQueue());
                System.out.println("Customer added: " + customer.getID());
                System.out.println(" Next customer: " + updatedServer.getWaitingCustomer().getID());
                System.out.println(oldServer.hasQueue());
                System.out.println();

                ServeEvent newSE = new ServeEvent(customer,
                                                  oldServer.getAvailableTime(),
                                                  linkedServerID);

                return Pair.of(shop.replace(updatedServer), newSE);
              });

        this.customer = customer;
        this.eventTime       = eventTime;
        this.linkedServerID  = linkedServerID;
    }

    public String toString() {
        
        return String.format("%.3f %d waits to be served by server %d", 
                             this.eventTime, 
                             this.customer.getID(), 
                             this.linkedServerID);
    }
}