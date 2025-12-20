package com.example.doctorservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.config.AppProperties;
import com.example.doctorservice.service.DoctorService;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppProperties appProperties;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("appName", appProperties.getName());
        config.put("version", appProperties.getVersion());
        config.put("maxDoctors", appProperties.getMaxDoctors());
        config.put("emailHost", appProperties.getEmail().getHost());
        return config;
    }
}
