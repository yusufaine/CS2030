package cs2030.simulator;

public class LeaveEvent extends Event {

    private final boolean isGreedy;

    LeaveEvent(Customer customer, double eventTime) {
        super(customer, eventTime, shop -> {
            return Pair.of(shop, new LeaveEvent(customer, eventTime));
        });
        
        this.isGreedy = customer.isGreedy();
    }

    /**
     * Returns a string representation of the object based on whether the customer
     * is greedy or not.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        
        if (this.isGreedy) {
            return String.format("%.3f %d(greedy) leaves", 
                             super.getCustomer().getArrivalTime(), 
                             super.getCustomer().getID());
        } else {
            return String.format("%.3f %d leaves", 
                             super.getCustomer().getArrivalTime(), 
                             super.getCustomer().getID());
        }
    }
}