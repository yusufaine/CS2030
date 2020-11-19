package cs2030.simulator;

import java.util.function.Function;
import java.util.ArrayList;

public abstract class Event {

    private final Customer customer;
    private final double eventTime;
    private final int linkedServerID;
    private final Function<Shop, Pair<Shop,Event>> execFunc;

    Event(Customer customer,
          double eventTime,
          int linkedServerID,
          Function<Shop, Pair<Shop,Event>> execFunc) {
        
        this.customer    = customer;
        this.eventTime   = eventTime;
        this.execFunc    = execFunc;
        this.linkedServerID = linkedServerID;
    }

    Event(Customer customer,
          double eventTime,
          Function<Shop, Pair<Shop,Event>> execFunc) {

        this.customer    = customer;
        this.eventTime   = eventTime;
        this.execFunc    = execFunc;
        this.linkedServerID = -1;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public int getLinkedServerID() {
        return this.linkedServerID;
    }

    public final Pair<Shop, Event> execute(Shop shop) {
        Pair<Shop,Event> result = this.execFunc.apply(shop);
        return result;
    }
}