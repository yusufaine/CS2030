package cs2030.simulator;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Map;

public class Shop {

    private final int numOfServer;
    private final List<Server> serverList;
    private final List<Customer> waitingList = new ArrayList<>();
    private final Map<Server, List<Customer>> serverMap = new HashMap<>();

    Shop(int numOfServer) {
        this.numOfServer = numOfServer;
        this.serverList  = createServerList(numOfServer);
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

    public List<Server> createServerList(int numOfServer) {

        List<Server> tmpList = new ArrayList<>();

        IntStream.range(1, numOfServer + 1)
                 .forEach(x -> {
                    Server tmpServer = new Server(x);
                    tmpList.add(tmpServer);
                 });

        return tmpList;
    }

    public Optional<Server> find(Predicate<Server> pred) {
        return this.getServerList()
                   .stream()
                   .filter(pred)
                   .findFirst();
    }

    public Shop replace(Server updatedServer) {

        List<Server> tmpList = new ArrayList<>();
        tmpList.addAll(this.getServerList());
        tmpList.set(updatedServer.getID() - 1, updatedServer);

        return new Shop(tmpList);
    }

    public List<Customer> getWaitingList() {
        return this.waitingList;
    }

    public Map<Server, List<Customer>> getServerMap() {
        return this.serverMap;
    }

    public void addBusyServer(Server server, Customer customer) {
        waitingList.add(customer);


        serverMap.put(server, waitingList);
    }

    public void removeCustomer(Server server, Customer customer) {

        waitingList.remove(customer);

        if (waitingList.size() == 0) {
            serverMap.remove(server);
        } else {
            serverMap.put(server, waitingList);
        }
    }

    public boolean isBusy(Server server) {
        if (serverMap.containsKey(server)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasQueue(Server server) {
        if (serverMap.get(server).size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return this.getServerList().toString();
    }
}