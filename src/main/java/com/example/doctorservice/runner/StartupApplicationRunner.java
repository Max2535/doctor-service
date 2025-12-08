package com.example.doctorservice.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)  // ทำงานหลัง CommandLineRunner
public class StartupApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=================================");
        System.out.println("ApplicationRunner is executing...");

        // Option arguments (--key=value)
        if (args.getOptionNames().size() > 0) {
            System.out.println("Option arguments:");
            args.getOptionNames().forEach(optionName -> {
                System.out.println("--" + optionName + "="
                    + args.getOptionValues(optionName));
            });
        }

        // Non-option arguments
        if (args.getNonOptionArgs().size() > 0) {
            System.out.println("Non-option arguments: "
                + args.getNonOptionArgs());
        }

        System.out.println("=================================");
    }
}