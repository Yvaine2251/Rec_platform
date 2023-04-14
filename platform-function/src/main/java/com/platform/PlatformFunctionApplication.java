package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PlatformFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformFunctionApplication.class, args);
    }

}
