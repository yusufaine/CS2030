package cs2030.simulator;

public class Customer {

    private final int customerID;
    private final double arrivalTime;

    /**
     * Constructs a new instance of a customer.
     *
     * @param      id    Identifier of the customer.
     * @param      time  Which the customer arrives.
     */
    public Customer(int id, double time) {
        this.customerID = id;
        this.arrivalTime = time;
    }

    public int getID() {
        return this.customerID;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return     String that states the specific customer arriving at what time.
     */
    @Override
    public String toString() {
        return String.format("%d arrives at %.1f", this.customerID, this.arrivalTime);
    }
}