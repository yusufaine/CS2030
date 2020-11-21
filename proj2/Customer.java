package cs2030.simulator;

import java.util.function.Supplier;

public class Customer {

    private final int customerID;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;
    private double computedServiceTime;
    private boolean isComputed = false;
    private final boolean isGreedy;

    /**
     * Constructs a new instance of a customer.
     *
     * @param      id           The identifier of the customer
     * @param      arrivalTime  The arrival time of the custoemr
     * @param      serviceTime  The time needed to service the customer
     * @param      isGreedy     Indicates if the customer is greedy
     */
    public Customer(int id, 
                    double arrivalTime, 
                    Supplier<Double> serviceTime, 
                    boolean isGreedy) {

        this.customerID  = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.isGreedy    = isGreedy;
    }

    /**
     * Constructs a new instance.
     *
     * @param      id           The identifier of the customer
     * @param      arrivalTime  The arrival time of the custoemr
     * @param      serviceTime  The time needed to service the customer
     */
    public Customer(int id, double arrivalTime, Supplier<Double> serviceTime) {
        this.customerID  = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.isGreedy    = false;
    }

    /**
     * Constructs a new instance.
     *
     * @param      id           The identifier of the customer
     * @param      arrivalTime  The arrival time of the custoemr
     */
    public Customer(int id, double arrivalTime) {
        this.customerID  = id;
        this.arrivalTime = arrivalTime;
        Supplier<Double> defaultTime = () -> 1.0;
        this.serviceTime = defaultTime;
        this.isGreedy    = false;
    }

    public int getID() {
        return this.customerID;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Gets the service time of the customer.
     *
     * @return     The service time of the customer.
     */
    public double getServiceTime() {
        
        if (this.isComputed == false) {
            this.isComputed = true;
            this.computedServiceTime = this.serviceTime.get();
        }
        return this.computedServiceTime;
    }

    public boolean isGreedy() {
        return this.isGreedy;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return     String that states the specific customer arriving at what time.
     */
    @Override
    public String toString() {
        return String.format("%d arrives at %.3f", this.customerID, this.arrivalTime);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof Customer) {
            Customer otherCustomer = (Customer)other;
            return this.getID() == otherCustomer.getID();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.customerID;
    }
}