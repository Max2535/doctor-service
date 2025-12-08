package com.example.doctorservice.service;

import com.example.doctorservice.config.AppProperties;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final AppProperties appProperties;

    public EmailService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void sendNotification(Object email, String subject, String body) {
        if (!appProperties.isEnableNotifications()) {
            System.out.println("Notifications are disabled");
            return;
        }

        System.out.println("Sending notification email...");
        System.out.println("SMTP Host: " + appProperties.getEmail().getHost());
        System.out.println("SMTP Port: " + appProperties.getEmail().getPort());
        System.out.println("From: " + appProperties.getEmail().getFrom());
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);


    }

    // public void sendEmail(String to, String subject, String body) {
    //     if (!appProperties.isEnableNotifications()) {
    //         System.out.println("Notifications are disabled");
    //         return;
    //     }

    //     System.out.println("Sending email...");
    //     System.out.println("SMTP Host: " + appProperties.getEmail().getHost());
    //     System.out.println("SMTP Port: " + appProperties.getEmail().getPort());
    //     System.out.println("From: " + appProperties.getEmail().getFrom());
    //     System.out.println("To: " + to);
    //     System.out.println("Subject: " + subject);
    //     System.out.println("Body: " + body);
    // }

    // public String getEmailConfiguration() {
    //     return String.format("Email Config - Host: %s, Port: %d, From: %s",
    //             appProperties.getEmail().getHost(),
    //             appProperties.getEmail().getPort(),
    //             appProperties.getEmail().getFrom());
    // }
}