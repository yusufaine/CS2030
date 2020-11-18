package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public class ServeEvent extends Event {

    /**
     * Defines the time needed to serve a customer.
     */
    private final double serveTime = 1.0;

    /**
     * Constructs a new ServeEvent instance.
     *
     * @param      customer     The current customer
     * @param      serverList   The server that all Event subclasses share
     * @param      eventTime    The tiem that the event happened
     * @param      linkedIndex  The index for the server that is serving the customer
     */
    ServeEvent(Customer customer, List<Server> serverList, double eventTime, int linkedIndex) {
        super(customer, 
              serverList, 
              serverList.get(linkedIndex).getAvailableTime(),
              linkedIndex);
    }

    /**
     * Overrides Event.execute() so that it can handle the execution of ServeEvent.
     * This method would return the respective events defined by the situation. 
     */
    @Override
    public Event execute() {
        Customer currentCustomer = super.getCustomer();

        int linkedIndex = super.getLinkedIndex();
        ArrayList<Server> currentSL = new ArrayList<>(super.getServerList());
        Server oldServer = currentSL.get(linkedIndex);


        /**
         * Updates the server's waiting customer boolean so that another customer
         * can queue up with this server.
         */
        if (oldServer.getWaitingCustomer() == true) {

            Server updatedServer = new Server(oldServer.getID(),
                                              oldServer.getAvailability(),
                                              false,
                                              oldServer.getAvailableTime() + serveTime,
                                              currentCustomer);

            currentSL.set(linkedIndex, updatedServer);


        } else {
            Server updatedServer = new Server(oldServer.getID(),
                                              oldServer.getAvailability(),
                                              oldServer.getWaitingCustomer(),
                                              oldServer.getAvailableTime() + serveTime,
                                              oldServer.getToServe());
            currentSL.set(linkedIndex, updatedServer);

        }


        /**
         * Returns a DoneEvent with the updated server list with the updated times
         * of the server.
         */
        return new DoneEvent(super.getCustomer(), 
                             currentSL, 
                             currentSL.get(linkedIndex).getAvailableTime(),
                             linkedIndex);
    }

    /**
     * Returns a string representation of the ServeEvent object.
     *
     * @return     String that states what time the specific customer is served by
     *             the specific server.
     */
    @Override
    public String toString() {

        Server printServer = super.getServerList().get(super.getLinkedIndex());
        Customer printCustomer = super.getCustomer();


        return String.format("%.3f %d served by %d", 
                                 super.getEventTime(),
                                 printCustomer.getID(),
                                 printServer.getID());
    }
}