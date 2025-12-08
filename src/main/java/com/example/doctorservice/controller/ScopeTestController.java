package com.example.doctorservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.service.PrototypeService;
import com.example.doctorservice.service.SingletonService;

@RestController
@RequestMapping("/api/test")
public class ScopeTestController {

    @Autowired
    private SingletonService singletonService;

    @Autowired
    private ApplicationContext context;

    @GetMapping("/singleton")
    public Map<String, Integer> testSingleton() {
        Map<String, Integer> result = new HashMap<>();
        result.put("call1", singletonService.incrementAndGet());
        result.put("call2", singletonService.incrementAndGet());
        // จะได้ 1, 2 (shared counter)
        return result;
    }

    @GetMapping("/prototype")
    public Map<String, Integer> testPrototype() {
        PrototypeService proto1 = context.getBean(PrototypeService.class);
        PrototypeService proto2 = context.getBean(PrototypeService.class);

        Map<String, Integer> result = new HashMap<>();
        result.put("proto1", proto1.incrementAndGet());
        result.put("proto2", proto2.incrementAndGet());
        // จะได้ 1, 1 (แต่ละ instance มี counter ของตัวเอง)
        return result;
    }
}
