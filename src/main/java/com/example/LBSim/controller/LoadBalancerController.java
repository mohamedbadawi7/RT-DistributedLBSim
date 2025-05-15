package com.example.LBSim.controller;
import com.example.LBSim.entity.Server;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LoadBalancerController {

    private List<Server> servers;

    public LoadBalancerController() {
        this.servers = new ArrayList<>();
        Server s = new Server(1);
        this.servers.add(s);
    }

    private int numServers() {
        return servers.size();
    }

    private void addServer() {
        Server s = new Server(numServers()+1);
        this.servers.add(s);
    }

    private void removeServer(Server s) {
        this.servers.remove(s);
    }
}
