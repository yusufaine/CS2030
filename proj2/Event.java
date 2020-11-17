package cs2030.simulator;

import java.util.function.Function;
import java.util.ArrayList;
// import java.util.function.Supplier;

public abstract class Event {

    private final Customer customer;
    private final double eventTime;
    private final int linkedServerID;
    private final Function<Shop, Pair<Shop,Event>> execFunc;
    private final ArrayList<Integer> busyServer = new ArrayList<>();

    Event(Customer customer,
          double eventTime,
          int linkedServerID,
          Function<Shop, Pair<Shop,Event>> execFunc) {
        
        this.customer    = customer;
        this.eventTime   = eventTime;
        this.linkedServerID = linkedServerID;
        this.execFunc    = execFunc;
    }

    Event(Customer customer,
          double eventTime,
          Function<Shop, Pair<Shop,Event>> execFunc) {

        this.customer    = customer;
        this.eventTime   = eventTime;
        this.linkedServerID = -1;
        this.execFunc    = execFunc;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public int getlinkedServerID() {
        return this.linkedServerID;
    }

    public final Pair<Shop, Event> execute(Shop shop) {
        Pair<Shop,Event> result = this.execFunc.apply(shop);
        return result;
    }
}