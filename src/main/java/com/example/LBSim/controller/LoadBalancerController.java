package com.example.LBSim.controller;
import com.example.LBSim.entity.Server;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
public class LoadBalancerController {

    private List<Server> servers;
    private int numRequests;

    public LoadBalancerController() {
        this.servers = new ArrayList<>();
        Server s = new Server(1);
        this.servers.add(s);
        numRequests = 0;
    }

    private int numServers() {
        return servers.size();
    }

    private int numRequests() {
        return numRequests;
    }

    private void incrRequests() {
        numRequests++;
    }

    private Server addServer() {
        Server s = new Server(numServers()+1);
        this.servers.add(s);
        return s;
    }

    private void removeServer(Server s) {
        this.servers.remove(s);
    }

    @PostMapping("/request")
    public void handleRequest() {

        incrRequests();

        Optional<Server> availableServer = servers.stream()
                .filter(Server::acceptsRequest)
                .findFirst();

        if (availableServer.isPresent()) {
            Server s = availableServer.get();
            System.out.println("Server " + s.getId() + " handling request number " + numRequests());
            s.handleRequest();
        } else {
            Server s = addServer();
            System.out.println("New server with id " + s.getId() + " created");
            System.out.println("Server " + s.getId() + " handling request number " + numRequests());
            s.handleRequest();
        }
    }

    @Scheduled(fixedRate = 10000)
    private void scale() {

        List<Server> workingServers = this.servers.stream().filter(Server::working).collect(Collectors.toList());
        workingServers.stream().map(s -> {
            if (s.notWorking()) {
                System.out.println("Server " + s.getId() + " has been removed");
                servers.remove(s);
            }
            return 1;
        });
    }

}
