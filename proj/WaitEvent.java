package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public class WaitEvent extends Event {


    /**
     * Constructs a new WaitEvent instance.
     *
     * @param      customer     The current customer
     * @param      serverList   The server that all Event subclasses share
     * @param      eventTime    The tiem that the event happened
     * @param      linkedIndex  The index for the server that is serving the customer
     */
    WaitEvent(Customer customer, List<Server> serverList, double eventTime, int linkedIndex) {
        super(customer, serverList, customer.getArrivalTime(), linkedIndex);
    }

    /**
     * Overrides Event.execute() so that it can handle the execution of WaitEvent.
     * This method would return the respective events defined by the situation. 
     * 
     * @return     ServeEvent with the updated values
     */    

    @Override
    public Event execute() {
        Customer customer = super.getCustomer();
        ArrayList<Server> currentSL = new ArrayList<>(super.getServerList());
        int linkedIndex = super.getLinkedIndex();

        Server linkedServer = currentSL.get(linkedIndex);
        Server updatedServer = new Server(linkedServer.getID(),
                                          linkedServer.getAvailability(),
                                          linkedServer.getWaitingCustomer(),
                                          linkedServer.getAvailableTime(),
                                          customer);

        Customer updatedCustomer = new Customer(customer.getID(), 
                                                updatedServer.getAvailableTime());

        return new ServeEvent(customer, 
                              currentSL, 
                              customer.getArrivalTime(),
                              linkedIndex);
    }

    /**
     * Returns a string representation of the WaitEvent object.
     *
     * @return     String that states what time the specific customer is waiting 
     *             to be served by the specific server.
     */
    @Override
    public String toString() {

        Server printServer = super.getServerList().get(super.getLinkedIndex());
        Customer printCustomer = super.getCustomer();

        return String.format("%.3f %d waits to be served by %d", 
                                 super.getEventTime(),
                                 printCustomer.getID(),
                                 printServer.getID());
    }
}