package com.example.doctorservice.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.doctorservice.config.AppProperties;

@Component
@Order(3)  // กำหนดลำดับการทำงาน (เลขน้อยทำงานก่อน)
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private AppProperties appProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏥 " + appProperties.getName() + " v" + appProperties.getVersion());
        System.out.println("=".repeat(50));
        System.out.println("✅ Application started successfully!");
        System.out.println("📧 Email: " + appProperties.getEmail().getHost());
        System.out.println("👤 Admin: " + appProperties.getAdmin().getName());
        System.out.println("=".repeat(50) + "\n");
    }
}
