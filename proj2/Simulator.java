package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import java.util.function.Supplier;


public class Simulator {
    private final ArrayList<Customer> customerList;
    private final PriorityQueue<Event> eventPQ;
    // private final PriorityQueue<Event> printPQ;
    private final Shop shop;
    private final Supplier<Double> serviceTime;

    public Simulator(ArrayList<Double> customerArrivals, 
                     int serverCount, 
                     Supplier<Double> serviceTime) {

        this.customerList = createCustomerList(customerArrivals, serviceTime);
        this.eventPQ      = new PriorityQueue<>(new EventComparator());
        // this.printPQ      = new PriorityQueue<>(new EventComparator());
        this.shop         = new Shop(serverCount);
        this.serviceTime  = serviceTime;
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
        int totalCustomer = 0;
        int customersLost = 0;
        double totalWaitTime = 0.0;

        populateEventPQ();

        while (eventPQ.peek() != null) {

            Event pollEvent = this.eventPQ.poll();
            System.out.println(this.shop.getServerMap());
            // System.out.println(pollEvent);

            if (pollEvent instanceof ArriveEvent) {
                
                totalCustomer++;

                Pair<Shop,Event> result = pollEvent.execute(this.shop); 
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof WaitEvent) {

                Pair<Shop,Event> result = pollEvent.execute(this.shop);
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof ServeEvent) {
                
                totalWaitTime += pollEvent.getEventTime() - 
                                 pollEvent.getCustomer().getArrivalTime();

                Pair<Shop,Event> result = pollEvent.execute(this.shop);
                eventPQ.offer(result.second());

            } else if (pollEvent instanceof DoneEvent) {
                Pair<Shop,Event> result = pollEvent.execute(this.shop);
            } else if (pollEvent instanceof LeaveEvent) {
                customersLost++;
            }
        }

        System.out.println(String.format("[%.3f %d %d]", 
                                 totalWaitTime / (totalCustomer - customersLost),
                                 totalCustomer - customersLost,
                                 customersLost));
    }
}