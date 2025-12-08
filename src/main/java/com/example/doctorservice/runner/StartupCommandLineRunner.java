package com.example.doctorservice.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)  // กำหนดลำดับการทำงาน (เลขน้อยทำงานก่อน)
public class StartupCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================");
        System.out.println("CommandLineRunner is executing...");
        System.out.println("Application started successfully!");

        // แสดง command-line arguments
        if (args.length > 0) {
            System.out.println("Command line arguments:");
            for (int i = 0; i < args.length; i++) {
                System.out.println("Arg[" + i + "]: " + args[i]);
            }
        }
        System.out.println("=================================");
    }
}