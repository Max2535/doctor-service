package com.example.doctorservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorservice.config.AppProperties;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class DoctorService {

    private final AppProperties appProperties;
    private final EmailService emailService;

    @Autowired
    public DoctorService(AppProperties appProperties,
                        EmailService emailService) {
        this.appProperties = appProperties;
        this.emailService = emailService;
    }

    @PostConstruct
    public void init() {
        System.out.println("=== DoctorService Initialized ===");
        System.out.println("Max Doctors: " + appProperties.getMaxDoctors());
        System.out.println("Notifications: " + appProperties.isEnableNotifications());
    }

    public void registerDoctor(String doctorName) {
        System.out.println("Registering doctor: " + doctorName);

        if (appProperties.isEnableNotifications()) {
            emailService.sendNotification(
                appProperties.getAdmin().getEmail(),
                "New Doctor Registered",
                "Doctor " + doctorName + " has been registered."
            );
        }
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("=== DoctorService Cleanup ===");
    }
}
