package com.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.platform.points.mapper")
@MapperScan("com.platform.mapper")
public class PointApplication {
    public static void main(String[] args) {

        SpringApplication.run(PointApplication.class,args);
        
    }
}
