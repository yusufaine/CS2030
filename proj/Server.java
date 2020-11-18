package cs2030.simulator;

public class Server {

    private final int serverID;
    private final boolean available;
    private final boolean waitingCustomer;
    private final double availableTime;
    private final Customer toServe;

    /**
     * Constructs a new instance of a server.
     *
     * @param      id               The id-number of the server
     * @param      available        Boolean reflecting the availability of the server
     * @param      waitingCustomer  Boolean representing if there is a waiting customer
     * @param      availableTime    Reflects when the server would be available
     * @param      toServe          The customer that it needs to serve after the current
     */
    public Server(int id, 
                  boolean available, 
                  boolean waitingCustomer, 
                  double availableTime, 
                  Customer toServe) {

        this.serverID = id;
        this.available = available;
        this.waitingCustomer = waitingCustomer;
        this.availableTime = availableTime;
        this.toServe = toServe;
    }

    /**
     * Constructs a new instance of a server.
     *
     * @param      id               The id-number of the server
     * @param      available        Boolean reflecting the availability of the server
     * @param      waitingCustomer  Boolean representing if there is a waiting customer
     * @param      availableTime    Reflects when the server would be available
     */
    public Server(int id, boolean available, boolean waitingCustomer, double availableTime) {
        this.serverID = id;
        this.available = available;
        this.waitingCustomer = waitingCustomer;
        this.availableTime = availableTime;
        this.toServe = null;
    }

    /**
     * Constructs a new instance of a server.
     *
     * @param      id               The id-number of the server
     */
    public Server(int id) {
        this.serverID = id;
        this.available = true;
        this.waitingCustomer = false;
        this.availableTime = 0.0;
        this.toServe = null;
    }


    public int getID() {
        return this.serverID;
    }

    public boolean getAvailability() {
        return this.available;
    }

    public boolean getWaitingCustomer() {
        return this.waitingCustomer;
    }

    public double getAvailableTime() {
        return this.availableTime;
    }

    public Customer getToServe() {
        return this.toServe;
    }

    /**
     * Returns a string representation of the Server object.
     *
     * @return     String that states whether the server is available, when is it next
     *             available, or if it has a customer waiting on it.
     */
    @Override
    public String toString() {
        if (this.getAvailability() == true) {
            return String.format("%d is available", 
                                 this.serverID);
        } else {
            if (this.getWaitingCustomer() == false) {
                return String.format("%d is busy; available at %.3f", 
                                     this.serverID, 
                                     this.availableTime);
            } else {
                return String.format("%d is busy; waiting customer to be served at %.3f", 
                                     this.serverID, 
                                     this.availableTime);
            }
        }
    }
}