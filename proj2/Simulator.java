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

    public Simulator(ArrayList<Double> customerArrivals, 
                     int serverCount, 
                     RNGImpl rng,
                     int maxQueue,
                     double restProb) {

        this.serviceTime  = () -> rng.genServiceTime();
        this.customerList = createCustomerList(customerArrivals, serviceTime);
        this.eventPQ      = new PriorityQueue<>(new EventComparator());
        this.printPQ      = new PriorityQueue<>(new EventComparator());
        this.shop         = new Shop(serverCount, maxQueue);
        this.rng          = rng;
        this.restProb     = restProb;
    }

    public ArrayList<Customer> createCustomerList(ArrayList<Double> customerArrivals,
                                                  Supplier<Double> serviceTime) {

        ArrayList<Customer> tmpList = new ArrayList<>();

        IntStream.range(1, customerArrivals.size() + 1)
                 .forEach(x -> {
                    Customer tmpCustomer = new Customer(x, 
                                                        customerArrivals.get(x - 1),
                                                        serviceTime);
                    tmpList.add(tmpCustomer);
                 });

        return tmpList;
    }


    public void populateEventPQ() {
        for (Customer customer : customerList) {
            ArriveEvent arriveEvent = new ArriveEvent(customer);
            eventPQ.offer(arriveEvent);
        }
    }

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
                int linkedServerID = nextEvent.getLinkedServerID();
                Server updatedServer = updatedShop.find(x->x.getID() == linkedServerID).get();

                if (this.rng.genRandomRest() < this.restProb) {
                    double restTime = this.rng.genRestPeriod();
                    ServerRestEvent newSRE = new ServerRestEvent(
                                                    nextEvent.getCustomer(),
                                                    nextEvent.getEventTime(),
                                                    nextEvent.getLinkedServerID(),
                                                    restTime);
                    eventPQ.offer(newSRE);

                } else if (updatedServer.hasQueue()) {

                    ServeEvent newSE = new ServeEvent(nextEvent.getCustomer(),
                                                      nextEvent.getEventTime(),
                                                      nextEvent.getLinkedServerID());
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
                int linkedServerID = nextEvent.getLinkedServerID();
                Server updatedServer = updatedShop.find(x->x.getID() == linkedServerID).get();

                if (updatedServer.hasQueue()) {
                    ServeEvent newSE = new ServeEvent(nextEvent.getCustomer(),
                                                      nextEvent.getEventTime(),
                                                      nextEvent.getLinkedServerID());
                    eventPQ.offer(newSE);
                }
            }
        }
        printEvents(printPQ);
        printStats(totalWaitTime, customersServed, customersLost);
    }

    public void printEvents(PriorityQueue<Event> printPQ) {
        for (Event e : printPQ) {
            System.out.println(e);
        }
    }

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