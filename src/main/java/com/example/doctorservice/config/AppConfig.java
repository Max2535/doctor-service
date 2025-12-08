package com.example.doctorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AppConfig {

    // สร้าง RestTemplate bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // สร้าง ObjectMapper bean with custom configuration
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // อื่นๆ configurations
        return mapper;
    }

    // // สร้าง bean ที่ต้องการ dependencies อื่น
    // @Bean
    // public DatabaseInitializer databaseInitializer(
    //         DoctorRepository doctorRepository,
    //         AppProperties appProperties) {
    //     return new DatabaseInitializer(doctorRepository, appProperties);
    // }

    // // Conditional bean creation
    // @Bean
    // @ConditionalOnProperty(name = "app.feature.email-enabled", havingValue = "true")
    // public EmailSender emailSender() {
    //     return new SmtpEmailSender();
    // }
}