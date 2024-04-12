package org.example.jobrecback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JobRecBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobRecBackApplication.class, args);
    }

}
