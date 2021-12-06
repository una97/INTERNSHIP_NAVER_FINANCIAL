package com.naver.autodeposit;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class AutoDepositBatchApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AutoDepositBatchApplication.class);
        application.run(args);
    }
}
