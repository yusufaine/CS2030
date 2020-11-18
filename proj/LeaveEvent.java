package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public class LeaveEvent extends Event {

    /**
     * Constructs a new LeaveEvent instance.
     *
     * @param      customer    The current customer
     * @param      serverList  The list of servers that is accessed by all Event subclasses
     * @param      eventTime   The time which this event occurs.
     */
    LeaveEvent(Customer customer, List<Server> serverList, double eventTime) {
        super(customer, serverList, customer.getArrivalTime());
    }

    @Override
    public Event execute() {
        return this;
    }

    /**
     * Returns a string representation of the LeaveEvent object.
     *
     * @return     String that states the time that the customer leaves.
     */
    @Override
    public String toString() {

        Customer printCustomer = super.getCustomer();

        return String.format("%.3f %d leaves", 
                                 printCustomer.getArrivalTime(),
                                 printCustomer.getID());
    }
}