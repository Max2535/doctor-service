package com.example.doctorservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    // Inject single value
    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    // Inject with default value (ถ้าไม่มีใน properties)
    @Value("${app.environment:development}")
    private String environment;

    // Inject number
    @Value("${app.max-doctors}")
    private int maxDoctors;

    // Inject boolean
    @Value("${app.enable-notifications}")
    private boolean enableNotifications;

    // Inject list (comma-separated)
    @Value("${app.supported-specializations}")
    private List<String> supportedSpecializations;

    // SpEL (Spring Expression Language)
    @Value("#{${app.db.pool.max-size} - ${app.db.pool.min-size}}")
    private int poolSizeRange;

    @GetMapping("/info")
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> config = new HashMap<>();
        config.put("appName", appName);
        config.put("version", appVersion);
        config.put("environment", environment);
        config.put("maxDoctors", maxDoctors);
        config.put("notificationsEnabled", enableNotifications);
        config.put("supportedSpecializations", supportedSpecializations);
        config.put("poolSizeRange", poolSizeRange);
        return config;
    }
}