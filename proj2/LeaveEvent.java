package cs2030.simulator;

public class LeaveEvent extends Event {

    private final Customer customer;
    private final double  eventTime;

    LeaveEvent(Customer customer, double eventTime){
        super(customer, 
              eventTime, 
              shop -> {
                return Pair.of(shop, new LeaveEvent(customer, eventTime));
              });

        this.customer    = customer;
        this.eventTime   = eventTime;
    }

    public String toString() {
        
        return String.format("%.3f %d leaves", 
                             super.getCustomer().getArrivalTime(), 
                             super.getCustomer().getID());
    }
}