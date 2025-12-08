package com.example.doctorservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

// 3. Request - หนึ่ง instance ต่อ HTTP request
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class RequestScopedService {
    private String requestId;

    public void setRequestId(String id) {
        this.requestId = id;
    }

    public String getRequestId() {
        return requestId;
    }
}
