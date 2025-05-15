package com.example.LBSim.entity;
import jakarta.persistence.Entity;

@Entity
public class Server {
    
    private int id;
    private int capacity;
    private int currentLoad;
    private boolean isHealthy;

    public Server(int id) {
        this.id = id;
        this.capacity = 100;
        this.currentLoad = 0;
        this.isHealthy = true;
    }

    public int getCapacity() {
        return capacity;
    }

    public void incrementLoad() {
        currentLoad++;
    }

    public void decrementLoad() {
        currentLoad--;
    }

    public boolean atCapacity() {
        return currentLoad >= capacity;
    }


}
