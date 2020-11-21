package cs2030.simulator;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.Comparator;

public class Shop {

    private final int numOfServer;
    private final List<Server> serverList;
    private final List<Customer> waitingList = new ArrayList<>();

    Shop(int numOfServer, int maxQueue, int selfCheckCount) {
        this.numOfServer = numOfServer;
        this.serverList  = createSL(numOfServer, maxQueue, selfCheckCount);
    }

    Shop(int numOfServer) {
        this.numOfServer = numOfServer;
        this.serverList  = createSL(numOfServer, 1, 0);
    }

    Shop(List<Server> serverList) {
        this.numOfServer = serverList.size();
        this.serverList  = serverList;
    }

    public int getNumOfServer() {
        return this.numOfServer;
    }

    public List<Server> getServerList() {
        return this.serverList;
    }

    /**
     * Creates the list of servers associated to the shop.
     *
     * @param      numOfServer     The number of server
     * @param      maxQueue        The maximum queue
     * @param      selfCheckCount  The self check count
     *
     * @return     Creates and returns a list of servers in the shop
     */
    public List<Server> createSL(int numOfServer, int maxQueue, int selfCheckCount) {


        List<Server> tmpList = new ArrayList<>();

        IntStream.rangeClosed(1, numOfServer)
                 .forEach(x -> {
                     tmpList.add(new Server(x, maxQueue, true));
                 });

        if (selfCheckCount > 0) {
            IntStream.rangeClosed(1, selfCheckCount)
                     .forEach(x -> {
                         tmpList.add(new Server(tmpList.size() + 1, maxQueue, false));
                     });
        }

        return tmpList;
    }

    /**
     * Searches for the first match of a server based on the predicate.
     *
     * @param      pred  Use the Server methods to assist with this
     *
     * @return     An optional of a server based on the predicate supplied.
     */
    public Optional<Server> find(Predicate<Server> pred) {
        return this.getServerList()
                   .stream()
                   .filter(pred)
                   .findFirst();
    }

    /**
     * Replaces the server in the shop with the updated server. 
     * This mehtod replaces the server of the same ID.
     *
     * @param      updatedServer  The updated server
     *
     * @return     The shop with the updated server.
     */
    public Shop replace(Server updatedServer) {

        List<Server> tmpList = new ArrayList<>();
        tmpList.addAll(this.getServerList());
        tmpList.set(updatedServer.getID() - 1, updatedServer);

        return new Shop(tmpList);
    }

    /**
     * Gets the first server with the shortest queue.
     *
     * @return     The first server with the shortest queue.
     */
    public Server getShortestQueue() {
        return this.getServerList()
                   .stream()
                   .min(Comparator.comparingInt(Server::getQueueSize))
                   .get();
    }

    @Override
    public String toString() {
        return this.getServerList().toString();
    }
}