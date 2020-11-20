package cs2030.simulator;

import java.util.Queue;
import java.util.LinkedList;

public class Server {

    private final int id;
    private final boolean available;
    private final boolean hasWaiting;
    private final double availableTime;
    private final int maxQueue;
    private final Queue<Customer> customerQueue = new LinkedList<>();

    Server(int id, 
           boolean available, 
           boolean hasWaiting, 
           double availableTime,
           int maxQueue) {

        this.id = id;
        this.available = available;
        this.hasWaiting = hasWaiting;
        this.availableTime = availableTime;
        this.maxQueue = maxQueue;
    }

    Server(int id, boolean available, boolean hasWaiting, double availableTime) {
        this.id = id;
        this.available = available;
        this.hasWaiting = hasWaiting;
        this.availableTime = availableTime;
        this.maxQueue = 1;
    }

    Server(int id, int maxQueue) {
        this.id = id;
        this.available = true;
        this.hasWaiting = false;
        this.availableTime = 0.0;
        this.maxQueue = maxQueue;
    }

    public int getID() {
        return this.id;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public boolean hasWaiting() {
        return this.hasWaiting;
    }

    public double getAvailableTime() {
        return this.availableTime;
    }

    public int getMaxQueue() {
        return this.maxQueue;
    }

    public Queue<Customer> getQueue() {
        return this.customerQueue;
    }

    public boolean isQueueFull() {
        return this.customerQueue.size() == maxQueue;
    }

    public void copyQueue(Server server) {
        this.customerQueue.addAll(server.getQueue());
    }

    public void addToQueue(Customer customer) {
        this.customerQueue.offer(customer);
    }

    public Customer peekNextCustomer() {
        return this.customerQueue.peek();
    }

    public Customer pollNextCustomer() {
        return this.customerQueue.poll();
    }

    public boolean hasQueue() {
        if (this.customerQueue.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (this.isAvailable()) {
            return String.format("%d is available", 
                                 this.id);
        } else {
            if (this.hasWaiting()) {
                return String.format("%d is busy; " + 
                                     "waiting customer to be served at %.3f", 
                                     this.id, 
                                     this.availableTime);
            } else {
                return String.format("%d is busy; available at %.3f", 
                                     this.id, 
                                     this.availableTime);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof Server) {
            Server otherServer = (Server)other;
            return this.getID() == otherServer.getID();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getID();
    }
}