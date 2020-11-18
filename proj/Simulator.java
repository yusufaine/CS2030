package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Simulator {
    private final ArrayList<Customer> customerList;
    private final ArrayList<Double> customerArrivals;
    private final ArrayList<Server> serverList;
    private final PriorityQueue<Event> eventPQ;
    private final PriorityQueue<Event> printPQ;

    /**
     * Constructs a new Simulator instance.
     *
     * @param      customerArrivals  List of customer arrival times
     * @param      numOfServers      The number of servers
     */
    public Simulator(ArrayList<Double> customerArrivals, int numOfServers) {

        this.customerArrivals = customerArrivals;
        this.customerList = createCustomerList(customerArrivals);
        this.serverList = createServerList(numOfServers);
        this.eventPQ = new PriorityQueue<>(new EventComparator());
        this.printPQ = new PriorityQueue<>(new EventComparator());
    }

    /**
     * Creates a customer list that would assist in the creation of ArriveEvent.
     *
     * @param      customerArrivals  List of customer arrival times
     *
     * @return     ArrayList of Customers to process through
     */
    public ArrayList<Customer> createCustomerList(ArrayList<Double> customerArrivals) {

        ArrayList<Customer> tmpList = new ArrayList<>();

        for (int i = 0; i < customerArrivals.size(); i++) {
            
            Customer newCustomer = new Customer(i + 1, customerArrivals.get(i));
            tmpList.add(newCustomer);
        }

        return tmpList;
    }

    /**
     * Creates an ArrayList of Server that would be used by the Simulator.
     *
     * @param      numOfServers  The number of servers
     *
     * @return     ArrayList of Server that would serve the Customer
     */
    public ArrayList<Server> createServerList(int numOfServers) {

        ArrayList<Server> tmpList = new ArrayList<>();

        for (int i = 0; i < numOfServers; i++) {
            Server addServer = new Server(i + 1);
            tmpList.add(addServer);
        }

        return tmpList;
    }

    /**
     * This is solely used to populate the Event PriorityQueue with the ArriveEvents
     * so that it would be handled chronologically. Tie breaker would be the smaller 
     * customer ID.
     */
    public void populateEventPQ() {

        for (Customer customer : customerList) {
            ArriveEvent arriveEvent = new ArriveEvent(customer, this.serverList);
            eventPQ.offer(arriveEvent);
        }
    }

    /**
     * Runs the simulation based on the elements in EventPQ. This would follow the 
     * program flow of the situation. All events begin as ArriveEvent before changing
     * to another Event subclass based on the attributes of the server in Server List.
     */
    public void run() {

        int totalCustomer = 0;
        int customersLost = 0;
        double totalWaitTime = 0.0;
        
        populateEventPQ();

        /**
         * This iterates through the eventPQ by polling the first element and running
         * it through the simulator using a series of instanceof checks to see what 
         * event it is now, and what event it should transition to. At every event 
         * (except ArriveEvent and LeaveEvent), this.serverList is updated so that
         * subsequent events are using the most updated servers.
         * 
         * So long as an event.execute() is not an instance of LeaveEvent or DoneEvent,
         * it is added into the eventPQ again so that it can be executed until it is done
         * or if it leaves.
         * 
         * While the eventPQ is not empty, each element is added into printPQ which would
         * log every event and its transition and sort it according to their event times.
         * The tie-breaker would be the event with the lower customer ID.
         */
        while (eventPQ.peek() != null) {
            
            Event pollEvent = this.eventPQ.poll();
            printPQ.offer(pollEvent);

            if (pollEvent instanceof ArriveEvent) {

                totalCustomer++;

                Event nextEvent = pollEvent.execute();
                eventPQ.offer(nextEvent);
            
            } else if (pollEvent instanceof WaitEvent) {

                Event nextEvent = pollEvent.execute();
                int linkedIndex = nextEvent.getLinkedIndex();
                Server linkedServer = nextEvent.getServerList().get(linkedIndex);

                this.serverList.set(linkedIndex, linkedServer);
                eventPQ.offer(nextEvent);

            } else if (pollEvent instanceof ServeEvent) {

                totalWaitTime += pollEvent.getEventTime() - 
                                 pollEvent.getCustomer().getArrivalTime();

                Event nextEvent = pollEvent.execute();
                int linkedIndex = nextEvent.getLinkedIndex();
                Server linkedServer = nextEvent.getServerList().get(linkedIndex);

                this.serverList.set(linkedIndex, linkedServer);
                eventPQ.offer(nextEvent);

            } else if (pollEvent instanceof DoneEvent) {

                int linkedIndex = pollEvent.getLinkedIndex();
                Server linkedServer = pollEvent.getServerList().get(linkedIndex);


                if (linkedServer.getWaitingCustomer() == false) {

                    Server updatedServer = new Server(linkedServer.getID());

                    this.serverList.set(linkedIndex, updatedServer);
                } else {

                    this.serverList.set(linkedIndex, linkedServer);
                }
            } else if (pollEvent instanceof LeaveEvent) {
                customersLost++;
            }
        }

        for (Event element : printPQ) {
            System.out.println(element);
        }

        System.out.println(String.format("[%.3f %d %d]", 
                                 totalWaitTime / (totalCustomer - customersLost),
                                 totalCustomer - customersLost,
                                 customersLost));

    }
}