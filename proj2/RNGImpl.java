package cs2030.simulator;

import cs2030.simulator.RandomGenerator;

public class RNGImpl {

    private final RandomGenerator rng;

    public RNGImpl(int seed, double lambda, double mu, double rho) {
        this.rng = new RandomGenerator(seed, lambda, mu, rho);
    }

    public double genInterArrivalTime() {
        return this.rng.genInterArrivalTime();
    }

    public double genServiceTime() {
        return this.rng.genServiceTime();
    }

    public double genRandomRest() {
        return this.rng.genRandomRest();
    }

    public double genRestPeriod() {
        return this.rng.genRestPeriod();
    }

    public double genCustomerType() {
        return this.rng.genCustomerType();
    }
}