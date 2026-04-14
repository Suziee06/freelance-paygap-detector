package com.detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.detector", "com.freelancedetector"})
@EntityScan(basePackages = "com.freelancedetector.entity")
@EnableJpaRepositories(basePackages = "com.freelancedetector.repository")
public class FreelanceDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreelanceDetectorApplication.class, args);
    }
}
