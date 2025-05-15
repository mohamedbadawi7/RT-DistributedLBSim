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
    private int requestsHandled;

    public Server() {}

    public Server(int id) {
        this.id = id;
        this.capacity = (int)(Math.random()*11);
        this.currentLoad = 0;
        this.isHealthy = true;
        requestsHandled = 0;
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

    private void incrementRequestsHandled() {
        requestsHandled++;
    }

    public boolean acceptsRequest() {
        return !atCapacity() && isHealthy;
    }

    public boolean notWorking() {
        if (currentLoad == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean working() {
        return currentLoad > 0;
    }

    public void setHealth() {

        if (currentLoad >= 0.9 * capacity) {
            isHealthy = false;
        }

        if (Math.random() < 0.02) {
            isHealthy = false;
        }

    }

    public boolean isNotHealthy() {
        return !isHealthy;
    }

    public int handleRequest() {

        if (acceptsRequest()) {
            incrementLoad();
            System.out.println("Server " + "#" + id + " has accepted the request");
            System.out.println("Server " + "#" + id + " - " + currentLoad + "/" + capacity);

            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 3600000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    decrementLoad();
                    incrementRequestsHandled();
                }
            });
            return 1;
        } else {
            System.out.println("Server " + "#" + id + " has not accepted the request");
            return -1;
        }
    }

    public String status() {
        return "Server " + getId() + ":                                     " + currentLoad + "/" + capacity + "                                                                        " + requestsHandled;
    }


}
