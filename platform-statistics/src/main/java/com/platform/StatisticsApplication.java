package com.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yjj
 * @date 2022/10/11-11:51
 */
@SpringBootApplication
@MapperScan("com.platform.*.mapper")
@MapperScan("com.platform.mapper")
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplication.class, args);
    }
}
