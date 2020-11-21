import cs2030.simulator.Simulator;
import cs2030.simulator.RNGImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.function.Supplier;

public class Main {

    /**
     * Entry point of the Simulator. Arguments required are: 
     * 1)  RNG Seed value,
     * 2)  Number of servers,
     * 3)  Number of self-checkout counters,
     * 4)  Maximum length of the queue,
     * 5)  Number of customers,
     * 6)  Arrival rate of the customers,
     * 7)  Service rate -- determines how fast a customer gets served
     * 8)  Resting rate -- determines the RNG value to determine if a server rests
     * 9)  Probablity of resting -- If RNG < probability, server rests
     * 10) Probability of greedy customer -- If RNG < probability, customer is greedy
     *
     * @param      args  The arguments that needs to be provided.
     */
    public static void main(String[] args) {

        int seed;
        int serverCount;
        int selfCheckCount = 0;
        int maxQueue       = 1;
        int customerCount;
        double lambda;
        double mu;
        double rho         = 0.0;
        double restProb    = 0.0;
        double greedyProb  = 0.0;

        if (args.length == 5) {
            seed           = Integer.parseInt(args[0]);
            serverCount    = Integer.parseInt(args[1]);
            customerCount  = Integer.parseInt(args[2]);
            lambda         = Double.parseDouble(args[3]);
            mu             = Double.parseDouble(args[4]);
        } else if (args.length == 6) {
            seed           = Integer.parseInt(args[0]);
            serverCount    = Integer.parseInt(args[1]);
            maxQueue       = Integer.parseInt(args[2]);
            customerCount  = Integer.parseInt(args[3]);
            lambda         = Double.parseDouble(args[4]);
            mu             = Double.parseDouble(args[5]);
        } else if (args.length == 8) {
            seed           = Integer.parseInt(args[0]);
            serverCount    = Integer.parseInt(args[1]);
            maxQueue       = Integer.parseInt(args[2]);
            customerCount  = Integer.parseInt(args[3]);
            lambda         = Double.parseDouble(args[4]);
            mu             = Double.parseDouble(args[5]);
            rho            = Double.parseDouble(args[6]);
            restProb       = Double.parseDouble(args[7]);
        } else if (args.length == 9) {
            seed           = Integer.parseInt(args[0]);
            serverCount    = Integer.parseInt(args[1]);
            selfCheckCount = Integer.parseInt(args[2]);
            maxQueue       = Integer.parseInt(args[3]);
            customerCount  = Integer.parseInt(args[4]);
            lambda         = Double.parseDouble(args[5]);
            mu             = Double.parseDouble(args[6]);
            rho            = Double.parseDouble(args[7]);
            restProb       = Double.parseDouble(args[8]);
        } else {
            seed           = Integer.parseInt(args[0]);
            serverCount    = Integer.parseInt(args[1]);
            selfCheckCount = Integer.parseInt(args[2]);
            maxQueue       = Integer.parseInt(args[3]);
            customerCount  = Integer.parseInt(args[4]);
            lambda         = Double.parseDouble(args[5]);
            mu             = Double.parseDouble(args[6]);
            rho            = Double.parseDouble(args[7]);
            restProb       = Double.parseDouble(args[8]);
            greedyProb     = Double.parseDouble(args[9]);
        }

        RNGImpl rng = new RNGImpl(seed, lambda, mu, rho);

        ArrayList<Double> customerArrivals = new ArrayList<>();

        IntStream.rangeClosed(1, customerCount)
                 .forEach(x -> {                    
                     if (x == 1) {
                         customerArrivals.add(0.0);
                     } else {
                         double rngArrival = rng.genInterArrivalTime();
                         int    lastIndex  = customerArrivals.size() - 1;
                         double lastEntry  = customerArrivals.get(lastIndex);
                         customerArrivals.add(lastEntry + rngArrival);
                     }
                 });

        Simulator sim = new Simulator(customerArrivals, 
                                      serverCount,
                                      rng,
                                      maxQueue,
                                      restProb,
                                      selfCheckCount,
                                      greedyProb);
        sim.run();
    }
}