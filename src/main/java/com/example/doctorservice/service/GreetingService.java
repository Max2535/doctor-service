package com.example.doctorservice.service;

import org.springframework.stereotype.Service;

@Service  // บอก Spring ว่านี่คือ Service bean
public class GreetingService {

    public String greet(String name) {
        return "Hello from Service, " + name + "!";
    }

    public String getWelcomeMessage() {
        return "Welcome to Doctor Management System";
    }
}