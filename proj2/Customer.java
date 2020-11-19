package cs2030.simulator;

import java.util.function.Supplier;

public class Customer {

    private final int customerID;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;
    private double computedServiceTime;
    private boolean isComputed = false;

    /**
     * Constructs a new instance of a customer.
     *
     * @param      id    Identifier of the customer.
     * @param      time  Which the customer arrives.
     */

    public Customer(int id, double arrivalTime, Supplier<Double> serviceTime) {
        this.customerID  = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Customer(int id, double arrivalTime) {
        this.customerID  = id;
        this.arrivalTime = arrivalTime;
        Supplier<Double> defaultTime = () -> 1.0;
        this.serviceTime = defaultTime;
    }

    public int getID() {
        return this.customerID;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public double getServiceTime() {
        
        if (this.isComputed == false) {
            this.isComputed = true;
            this.computedServiceTime = this.serviceTime.get();
        }
        return this.computedServiceTime;
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