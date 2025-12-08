package com.example.doctorservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.config.AppProperties;
import com.example.doctorservice.service.EmailService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final EmailService emailService;
    private final AppProperties appProperties;

    // Constructor Injection - Best Practice
    public DoctorController(
            EmailService emailService,
            AppProperties appProperties) {
        this.emailService = emailService;
        this.appProperties = appProperties;
    }

    public String getDoctors() {
        return "List of doctors";
    }

    public void sendNotification(String message) {
        emailService.sendEmail("admin@example.com", "Notification", message);
    }

    public String getAppName() {
        return appProperties.getName();
    }
}