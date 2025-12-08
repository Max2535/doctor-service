package com.example.doctorservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

// 2. Prototype - สร้าง instance ใหม่ทุกครั้งที่ request
@Service
@Scope("prototype")
public class PrototypeService {
    private int counter = 0;

    public int incrementAndGet() {
        return ++counter;  // แต่ละ instance มี counter ของตัวเอง
    }
}