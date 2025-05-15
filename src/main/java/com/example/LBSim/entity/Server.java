package com.example.LBSim.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Server {

    @Id
    private int id;

    private int capacity;
    private int currentLoad;
    private boolean isHealthy;

    public Server() {}

    public Server(int id) {
        this.id = id;
        this.capacity = 100;
        this.currentLoad = 0;
        this.isHealthy = true;
    }

    public int getId() {
        return id;
    }

    private int getCapacity() {
        return capacity;
    }

    private void incrementLoad() {
        currentLoad++;
    }

    private void decrementLoad() {
        currentLoad--;
    }

    private boolean atCapacity() {
        return currentLoad >= capacity;
    }

    public boolean acceptsRequest() {
        return atCapacity() && isHealthy;
    }

    public boolean notWorking() {
        return currentLoad == 0;
    }

    public boolean working() {
        return currentLoad > 0;
    }

    public int handleRequest() {

        if (acceptsRequest()) {
            incrementLoad();

            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 5000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    decrementLoad();
                }
            });
            return 1;
        } else {
            return -1;
        }
    }


}
