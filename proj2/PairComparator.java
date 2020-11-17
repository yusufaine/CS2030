package cs2030.simulator;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<Double,Double>> {
    @Override
    public int compare(Pair<Double,Double> p1, Pair<Double,Double> p2) {
        double p1arrival = p1.first();
        double p2arrival = p2.first();
        if (p1arrival < p2arrival) {
            return -1;
        } else {
            return 1;
        }
    }
}