package com.example.mymmal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.mymmal")
@MapperScan("com.example.mymmal.dao")
public class MymmalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MymmalApplication.class, args);
    }

}
