package com.example.doctorservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class LifecycleService {

    public LifecycleService() {
        System.out.println("1. Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("2. @PostConstruct - Bean initialized");
        // ทำ initialization logic ที่ต้องการ dependencies
        loadConfiguration();
        connectToExternalService();
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("3. @PreDestroy - Bean destroyed");
        // ทำ cleanup logic
        closeConnections();
        saveState();
    }

    private void loadConfiguration() {
        System.out.println("Loading configuration...");
    }

    private void connectToExternalService() {
        System.out.println("Connecting to external service...");
    }

    private void closeConnections() {
        System.out.println("Closing connections...");
    }

    private void saveState() {
        System.out.println("Saving state...");
    }

    public void doWork() {
        System.out.println("Doing work...");
    }
}
// ```
// **Lifecycle Order:**
// ```
// 1. Constructor
// 2. Dependency Injection
// 3. @PostConstruct
// 4. Bean ready to use
// 5. @PreDestroy (on shutdown)
// 6. Bean destroyed