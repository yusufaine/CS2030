package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {

    private final Customer customer;
    private final List<Server> serverList;
    private final double eventTime;

    private final int linkedIndex;

    /**
     * Defines the variables that its subclasses would need.
     * 
     * @param      customer    The customer for each Event subclass.
     * @param      serverList  The list of servers accessible by each Event subclass.
     * @param      eventTime   The time that the event occurs.
     * @param      linkedIndex  This is used to get the specific server that is
     *                          attending to the current customer.
     */
    Event(Customer customer, List<Server> serverList, double eventTime, int linkedIndex) {
        this.customer = customer;
        this.serverList = serverList;
        this.eventTime = eventTime;
        this.linkedIndex = linkedIndex;
    }

    Event(Customer customer, List<Server> serverList, double eventTime) {
        this.customer = customer;
        this.serverList = serverList;
        this.eventTime = eventTime;
        this.linkedIndex = -1;
    }


    public Customer getCustomer() {
        return this.customer;
    }

    public List<Server> getServerList() {
        return this.serverList;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public int getLinkedIndex() {
        return this.linkedIndex;
    }

    /**
     * Abstract method execute so that each subclass of this would need to implement
     * its own execute() method which is responsible of changing the event from one
     * state to another.
     */
    public abstract Event execute();
}