package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public class DoneEvent extends Event {

    /**
     * Constructs a new DoneEvent instance.
     *
     * @param      customer     The current customer
     * @param      serverList   The server list which all Event subclasses refer to
     * @param      eventTime    The time that the event happened
     * @param      linkedIndex  The linked index which is used to get the server that 
     *                          is currently serving the customer.
     */
    DoneEvent(Customer customer, List<Server> serverList, double eventTime, int linkedIndex) {
        super(customer, 
              serverList, 
              serverList.get(linkedIndex).getAvailableTime(), 
              linkedIndex);
    }

    @Override
    public Event execute() {
        int linkedIndex = super.getLinkedIndex();
        ArrayList<Server> currentSL = new ArrayList<>(super.getServerList());
        Server oldServer = currentSL.get(linkedIndex);        
        int serverID = oldServer.getID();

        Customer currentCustomer = super.getCustomer();

        if (oldServer.getWaitingCustomer() == true) {
            Server updatedServer = new Server(oldServer.getID(),
                                              false,
                                              false,
                                              oldServer.getAvailableTime(),
                                              oldServer.getToServe());

            currentSL.set(linkedIndex, updatedServer);

        } else {
            Server updatedServer = new Server(oldServer.getID(),
                                              true,
                                              false,
                                              oldServer.getAvailableTime(),
                                              null);

            currentSL.set(linkedIndex, updatedServer);
        }

        return new DoneEvent(currentCustomer, 
                             currentSL, 
                             currentSL.get(linkedIndex).getAvailableTime(),
                             linkedIndex);
    }

    @Override
    public String toString() {

        Server printServer = super.getServerList().get(super.getLinkedIndex());
        Customer printCustomer = super.getCustomer();

        return String.format("%.3f %d done serving by %d", 
                                 super.getEventTime(),
                                 printCustomer.getID(),
                                 printServer.getID());
    }
}