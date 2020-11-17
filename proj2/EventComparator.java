package cs2030.simulator;

import java.util.Comparator;

/**
 * This class describes an event comparator that would sort events chronologically.
 * If events have the same time, it would then sort it by the Customer's ID.
 */
public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event e1, Event e2) {
        double e1Time = e1.getEventTime();
        double e2Time = e2.getEventTime();
        
        if (e1Time < e2Time) {
            return -1;
        } else if (e1Time > e2Time) {
            return 1;
        } else {
            int e1CustomerID = e1.getCustomer().getID();
            int e2CustomerID = e2.getCustomer().getID();

            if (e1CustomerID < e2CustomerID) {
                return e1CustomerID - e2CustomerID;
            } else {
                return e1CustomerID - e2CustomerID;
            }
        }
    }
}