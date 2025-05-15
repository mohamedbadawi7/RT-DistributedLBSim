package com.example.LBSim.controller;
import com.example.LBSim.entity.Server;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/request")
    public void handleRequest() {

        incrRequests();
        System.out.println("Request # " + numRequests + " recieved...");

        Optional<Server> availableServer = servers.stream()
                .filter(Server::acceptsRequest)
                .findFirst();

        if (availableServer.isPresent()) {
            Server s = availableServer.get();
            System.out.println("Server " + s.getId() + " handling request number " + numRequests() + "...");
            s.handleRequest();
        } else {
            Server s = addServer();
            System.out.println("New server with id " + s.getId() + " created...");
            System.out.println("Server " + s.getId() + " handling request number " + numRequests() + "...");
            s.handleRequest();
        }
    }

    @Scheduled(fixedRate = 10000)
    private void scale() {

        List<Server> notWorkingServers = this.servers.stream().filter(Server::notWorking).collect(Collectors.toList());
        notWorkingServers.forEach(s -> {
                System.out.println("Server " + s.getId() + " has no requests. Removing...");
                servers.remove(s);
        });

        List<Server> unHealthyServers = this.servers.stream().filter(Server::isNotHealthy).collect(Collectors.toList());
        unHealthyServers.forEach(s -> {
            System.out.println("Server " + s.getId() + " is not healthy. Removing...");
            servers.remove(s);
        });
    }

    @Scheduled(fixedRate = 60000)
    private void report() {
        System.out.println("**********************************************System Report***************************************************************");
        System.out.println("Total Requests Received:                                                                                         " + numRequests);
        System.out.println("Number of servers:                                                                                               " + numServers());
        System.out.println("Server ID                      currentLoad   /   capacity                        # Requests Handled");
        this.servers.forEach(s -> System.out.println(s.status()));
        System.out.println("***************************************************************************************************************************");
    }

    @Scheduled(fixedRate = 10000)
    private void healthCheck() {
        servers.forEach(s -> {s.setHealth();});
    }

}
