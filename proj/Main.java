import cs2030.simulator.Customer;
import cs2030.simulator.Server;
import cs2030.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    /**
     * The main function that would initiate the simulation.
     *
     */
    public static void main(String[] args) {

        ArrayList<Double> customerArrivals = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();

        /**
         * This populates the customerArrivals ArrayList with the double-type
         * user-inputs.
         */
        while (sc.hasNextDouble()) {
            double arrivalTime = sc.nextDouble();
            customerArrivals.add(arrivalTime);
        }

        /**
         * This would create a Simulator object that takes in the list of customer
         * arrivals and the number of servers defiend by the user-input so that 
         * it can begin the simulation of events that happens at sim.run(). 
         */
        Simulator sim = new Simulator(customerArrivals, numOfServers);
        sim.run();
    }
}