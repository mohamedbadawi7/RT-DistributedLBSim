package com.example.LBSim.controller;
import com.example.LBSim.entity.Server;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LoadBalancerController {

    private List<Server> servers;

    public LoadBalancerController(List<Server> servers) {
        this.servers = new ArrayList<>();
    }
}
