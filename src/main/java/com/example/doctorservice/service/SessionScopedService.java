package com.example.doctorservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

// 4. Session - หนึ่ง instance ต่อ HTTP session
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SessionScopedService {
    private String userId;

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getUserId() {
        return userId;
    }
}