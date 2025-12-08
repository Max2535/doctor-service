package com.example.doctorservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.service.GreetingService;

@RestController
public class HelloController {

    // Dependency Injection แบบ Constructor (แนะนำ!)
    private final GreetingService greetingService;

    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return greetingService.greet(name);
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greetingService.getWelcomeMessage();
    }
}