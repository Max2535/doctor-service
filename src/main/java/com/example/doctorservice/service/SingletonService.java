package com.example.doctorservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

// 1. Singleton (default) - หนึ่ง instance ต่อ application
@Service
@Scope("singleton")  // หรือไม่ต้องระบุก็ได้
public class SingletonService {
    private int counter = 0;

    public int incrementAndGet() {
        return ++counter;  // shared across all requests
    }
}
