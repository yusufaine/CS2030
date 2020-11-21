package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import java.util.function.Supplier;

import cs2030.simulator.RNGImpl;

public class Simulator {
    private final ArrayList<Customer> customerList;
    private final PriorityQueue<Event> eventPQ;
    private final PriorityQueue<Event> printPQ;
    private final Shop shop;
    private final RNGImpl rng;
    private final Supplier<Double> serviceTime;
    private final double restProb;
    private final double greedyProb;

    /**
     * Constructs a new instance of the Simulator.
     *
     * @param      customerArrivals  List of customer arrival times
     * @param      serverCount       Number of servers
     * @param      rng               The random number generator
     * @param      maxQueue          The maximum queue of the servers
     * @param      restProb          The resting probability
     * @param      selfCheckCount    The number of self-checkouts
     * @param      greedyProb        The greedy probability
     */
    public Simulator(ArrayList<Double> customerArrivals, 
                     int serverCount, 
                     RNGImpl rng,
                     int maxQueue,
                     double restProb,
                     int selfCheckCount,
                     double greedyProb) {

        this.rng          = rng;
        this.serviceTime  = () -> rng.genServiceTime();
        this.customerList = createCustomerList(customerArrivals, 
                                               serviceTime,
                                               greedyProb);
        this.eventPQ      = new PriorityQueue<>(new EventComparator());
        this.printPQ      = new PriorityQueue<>(new EventComparator());
        this.shop         = new Shop(serverCount, maxQueue, selfCheckCount);
        this.restProb     = restProb;
        this.greedyProb   = greedyProb;
    }

    /**
     * Creates a customer list using the list of arrival times.
     *
     * @param      customerArrivals  The customer arrivals
     * @param      serviceTime       The service time of the customers
     * @param      greedyProb        Used to determine if customer is greedy
     *
     * @return     List of customers that will be used in populateEventPQ();
     */
    public ArrayList<Customer> createCustomerList(ArrayList<Double> customerArrivals,
                                                  Supplier<Double> serviceTime,
                                                  double greedyProb) {
        ArrayList<Customer> tmpList = new ArrayList<>();

        IntStream.range(1, customerArrivals.size() + 1)
                 .forEach(customerID -> {
                     boolean isGreedy = this.rng.genCustomerType() < greedyProb;
                     tmpList.add(new Customer(customerID, 
                                              customerArrivals.get(customerID - 1), 
                                              serviceTime,
                                              isGreedy));
                 });

        return tmpList;
    }

    /**
     * Initialises the simulator with the necessary arrive events based on the 
     * customerList created earlier.
     * 
     */
    public void populateEventPQ() {
        for (Customer customer : customerList) {
            ArriveEvent arriveEvent = new ArriveEvent(customer);
            eventPQ.offer(arriveEvent);
        }
    }

    /**
     * Runs the simulation based on events in EventPQ.
     */
    public void run() {
        int totalCustomer    = 0;
        int customersLost    = 0;
        int customersServed  = 0;
        double totalWaitTime = 0.0;
        Shop updatedShop     = this.shop;

        populateEventPQ();

        while (eventPQ.peek() != null) {

            Event pollEvent = this.eventPQ.poll();
            printPQ.offer(pollEvent);

            if (pollEvent instanceof ArriveEvent) {
                
                totalCustomer++;

                Pair<Shop,Event> result = pollEvent.execute(updatedShop); 

                updatedShop = result.first();
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof WaitEvent) {

                Pair<Shop,Event> result = pollEvent.execute(updatedShop);

                updatedShop = result.first();

            } else if (pollEvent instanceof ServeEvent) {
                
                customersServed++;

                totalWaitTime += pollEvent.getEventTime() - 
                                 pollEvent.getCustomer().getArrivalTime();

                Pair<Shop,Event> result = pollEvent.execute(updatedShop);

                updatedShop = result.first();
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof DoneEvent) {

                Pair<Shop,Event> result = pollEvent.execute(updatedShop);

                updatedShop = result.first();

                Event nextEvent = result.second();
                int linkedServerID = nextEvent.getLinkedServer().getID();
                Server updatedServer = updatedShop.find(x -> x.getID() == linkedServerID).get();

                if (nextEvent.getLinkedServer().isHuman()) {
                    if (this.rng.genRandomRest() < this.restProb) {
                        double restTime = this.rng.genRestPeriod();
                        ServerRestEvent newSRE = new ServerRestEvent(
                                                    nextEvent.getCustomer(),
                                                    nextEvent.getEventTime(),
                                                    nextEvent.getLinkedServer(),
                                                    restTime);
                        eventPQ.offer(newSRE);
                        
                    } else if (updatedServer.hasQueue()) {

                        ServeEvent newSE = new ServeEvent(nextEvent.getCustomer(),
                                                          nextEvent.getEventTime(),
                                                          nextEvent.getLinkedServer());
                        eventPQ.offer(newSE);
                        
                    }
                } else if (updatedServer.hasQueue()) {

                    ServeEvent newSE = new ServeEvent(nextEvent.getCustomer(),
                                                      nextEvent.getEventTime(),
                                                      nextEvent.getLinkedServer());
                    eventPQ.offer(newSE);
                }

            } else if (pollEvent instanceof LeaveEvent) {
                
                customersLost++;
            
            } else if (pollEvent instanceof ServerRestEvent) {
                printPQ.remove(pollEvent);

                Pair<Shop,Event> result = pollEvent.execute(updatedShop);
                
                updatedShop = result.first();
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof ServerBackEvent) {
                
                printPQ.remove(pollEvent);

                Pair<Shop,Event> result = pollEvent.execute(updatedShop);

                updatedShop = result.first();

                Event nextEvent = result.second();
                int linkedServerID = nextEvent.getLinkedServer().getID();
                Server updatedServer = updatedShop.find(x -> x.getID() == linkedServerID).get();

                if (updatedServer.hasQueue()) {
                    ServeEvent newSE = new ServeEvent(nextEvent.getCustomer(),
                                                      nextEvent.getEventTime(),
                                                      nextEvent.getLinkedServer());
                    eventPQ.offer(newSE);
                }
            }
        }
        printEvents(printPQ);
        printStats(totalWaitTime, customersServed, customersLost);
    }

    /**
     * Prints the events of the Simulator.
     *
     * @param      printPQ  Queue of events sorted chronologically
     */
    public void printEvents(PriorityQueue<Event> printPQ) {
        for (Event e : printPQ) {
            System.out.println(e);
        }
    }

    /**
     * Prints statistics of the simulation.
     *
     * @param      totalTime  The total time customers waited
     * @param      served     The customers served
     * @param      lost       The customers lost
     */
    public void printStats(double totalTime, int served, int lost) {

        double averageTime = 0;

        if (served == 0) {
            averageTime = 0;
        } else {
            averageTime = totalTime / served;
        }
        System.out.println(String.format("[%.3f %d %d]", 
                                         averageTime,
                                         served,
                                         lost));
    }
}