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
    private final boolean isHuman;
    private static final  Queue<Customer> sharedQueue = new LinkedList<>();

    Server(int id, 
           boolean available, 
           boolean hasWaiting, 
           double availableTime,
           int maxQueue,
           boolean isHuman) {

        this.id = id;
        this.available = available;
        this.hasWaiting = hasWaiting;
        this.availableTime = availableTime;
        this.maxQueue = maxQueue;
        this.isHuman = isHuman;
    }

    Server(int id, boolean available, boolean hasWaiting, double availableTime) {
        this.id = id;
        this.available = available;
        this.hasWaiting = hasWaiting;
        this.availableTime = availableTime;
        this.maxQueue = 1;
        this.isHuman = true;
    }

    Server(int id, int maxQueue, boolean isHuman) {
        this.id = id;
        this.available = true;
        this.hasWaiting = false;
        this.availableTime = 0.0;
        this.maxQueue = maxQueue;
        this.isHuman = isHuman;
    }

    /**
     * Gets the ID of the server.
     *
     * @return     The ID of the server.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Determines if the server is available.
     *
     * @return     Based on the event the server is in. True if available, False otherwise.
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * Determines if the server has any waiting customers.
     * Normal (human) servers would return their respectinve hasWaiting values.
     * Self-checkouts return the boolean of whether their shared queue size is > 0.
     *
     * @return     True if waiting, False otherwise.
     */
    public boolean hasWaiting() {
        if (this.isHuman()) {
            return this.hasWaiting;        
        } else {
            return this.sharedQueue.size() > 0;
        }
    }

    /**
     * Gets the next available time of the server.
     *
     * @return     The next available time.
     */
    public double getAvailableTime() {
        return this.availableTime;
    }

    /**
     * Gets the maximum queue length.
     *
     * @return     The maximum queue.
     */
    public int getMaxQueue() {
        return this.maxQueue;
    }

    /**
     * Determines if the server is a normal human server, 
     * or a self-checkout.
     *
     * @return     True if human, False if self-checkout.
     */
    public boolean isHuman() {
        return this.isHuman;
    }

    /**
     * If isHuman() == true, this gets the respective waiting customer queue.
     * Else, it gets the shared queue of the self-checkout.
     *
     * @return     The queue.
     */
    public Queue<Customer> getQueue() {
        if (this.isHuman()) {
            return this.customerQueue;
        } else {
            return this.sharedQueue;
        }
    }

    /**
     * Determines if the respective queue is full.
     *
     * @return     True if queue full, False otherwise.
     */
    public boolean isQueueFull() {
        if (this.isHuman()) {
            return this.customerQueue.size() == maxQueue;
        } else {
            return this.sharedQueue.size() == maxQueue;
        }
    }

    /**
     * Used to copy the queue of an old server over to a new one
     * during the transition of events and server to ensure that 
     * the queues are updated.
     * 
     * <p>Note: If used with addToQueue(), keep the sequence in mind.
     *
     * @param      server  The old server's queue which needs to be copied.
     */
    public void copyQueue(Server server) {
        if (this.isHuman()) {
            this.customerQueue.addAll(server.getQueue());
        }
    }

    /**
     * Adds a customer to the queue during the WaitEvent.
     * Used in-tendem with copyQueue() to ensure that the queues
     * are accurate.
     * 
     * <p>Note: Sequence of using this matters.
     *
     * @param      customer  The waiting customer.
     */
    public void addToQueue(Customer customer) {
        if (this.isHuman()) {
            this.customerQueue.offer(customer);
        } else {
            this.sharedQueue.offer(customer);
        }
    }

    /**
     * Used to determine which customer is to be served next.
     *
     * @return     The next customer in the queue.
     */
    public Customer peekNextCustomer() {
        if (this.isHuman()) {
            return this.customerQueue.peek();
        } else {
            return this.sharedQueue.peek();
        }
    }

    /**
     * Returns the next customer in the queue while removing it.
     *
     * @return     The next customer in the queue.
     */
    public Customer pollNextCustomer() {
        if (this.isHuman()) {
            return this.customerQueue.poll();
        } else {
            return this.sharedQueue.poll();
        }
    }

    /**
     * Determines if the respective types of servers have a queue.
     *
     * @return     True if queue, False otherwise.
     */
    public boolean hasQueue() {
        if (this.isHuman()) {
            return this.customerQueue.size() > 0;
        } else {
            return this.sharedQueue.size() > 0;
        }
    }

    /**
     * Gets the queue size of the respective types of servers.
     *
     * @return     The queue size.
     */
    public int getQueueSize() {
        if (this.isHuman()) {
            return this.customerQueue.size();
        } else {
            return this.sharedQueue.size();
        }
    }

    /**
     * Returns a string representation of the states of a server.
     *
     * @return     String representation of the states of a server.
     */
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
}