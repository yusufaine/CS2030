package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public class ArriveEvent extends Event {

    /**
     * Constructs a new ArriveEvent instance.
     *
     * @param      customer    The current customer
     * @param      serverList  The server list that all Event subclasses refer to
     */
    ArriveEvent(Customer customer, List<Server> serverList) {
        super(customer, serverList, customer.getArrivalTime());
    }

    /**
     * Overrides Event.execute() so that it can handle the execution of
     * ArriveEvent. This method would return the respective events defined by 
     * the situation. 
     */
    @Override
    public Event execute() {
        Customer customer = super.getCustomer();
        ArrayList<Server> currentSL = new ArrayList<>(super.getServerList());

        /**
         * This checks to see if there are any available servers by iterating through
         * the server list and checking each server's availbility boolean value.
         * 
         * If there is an available server, this would return a ServeEvent that
         * uses that respecive server and change its availability to false.
         */
        for (Server server : currentSL) {
            if (server.getAvailability() == true) {
                int serverID = server.getID();
                int linkedIndex = serverID - 1;
                double updatedTime = customer.getArrivalTime();
                Server updatedServer = new Server(serverID,
                                                  false,
                                                  server.getWaitingCustomer(),
                                                  updatedTime,
                                                  server.getToServe());

                currentSL.set(linkedIndex, updatedServer);

                return new ServeEvent(customer, 
                                      currentSL, 
                                      customer.getArrivalTime(), 
                                      linkedIndex);
            }
        }

        for (Server server : currentSL) {
            if (server.getAvailability() == false) {

                /**
                 * If there are no servers available when the customer arrives,
                 * this would return a WaitEvent so that the customer would be
                 * served by the first server in the list which does not have a 
                 * waiting customer attached to it.
                 */
                if (server.getWaitingCustomer() == false) {
                    int serverID = server.getID();
                    int linkedIndex = serverID - 1;
                    Server updatedServer = new Server(server.getID(),
                                                  false,
                                                  true,
                                                  server.getAvailableTime(),
                                                  customer);

                    currentSL.set(linkedIndex, updatedServer);

                    return new WaitEvent(customer, 
                                         currentSL, 
                                         customer.getArrivalTime(), 
                                         linkedIndex);
                }
            }
        }

        /**
         * If all the servers are busy and has a customer waiting on them, 
         * this would return a LeaveEvent.
         */
        return new LeaveEvent(customer, 
                              currentSL, 
                              customer.getArrivalTime());
    }

    /**
     * Returns a string representation of the ArriveEvent object.
     *
     * @return     String that states what time the customer arrives.
     */
    @Override
    public String toString() {

        Customer printCustomer = super.getCustomer();

        return String.format("%.3f %d arrives", 
                             super.getEventTime(),
                             printCustomer.getID());
    }
}