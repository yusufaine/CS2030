package cs2030.simulator;

import java.util.function.Function;
import java.util.ArrayList;

public abstract class Event {

    private final Customer customer;
    private final double eventTime;
    private final Server linkedServer;
    private final Function<Shop, Pair<Shop,Event>> execFunc;

    Event(Customer customer,
          double eventTime,
          Server linkedServer,
          Function<Shop, Pair<Shop,Event>> execFunc) {
        
        this.customer     = customer;
        this.eventTime    = eventTime;
        this.execFunc     = execFunc;
        this.linkedServer = linkedServer;
    }

    Event(Customer customer,
          double eventTime,
          Function<Shop, Pair<Shop,Event>> execFunc) {

        this.customer    = customer;
        this.eventTime   = eventTime;
        this.execFunc    = execFunc;
        this.linkedServer = null;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public int getCustomerID() {
        return this.customer.getID();
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public Server getLinkedServer() {
        return this.linkedServer;
    }

    public final Pair<Shop, Event> execute(Shop shop) {
        Pair<Shop,Event> result = this.execFunc.apply(shop);
        return result;
    }
}