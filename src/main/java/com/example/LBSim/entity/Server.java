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

    public int handleRequest() {

        if (isHealthy && !atCapacity()) {
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
