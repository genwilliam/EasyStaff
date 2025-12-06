package com.example.easystaff;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.easystaff.mapper")
public class EasyStaffApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyStaffApplication.class, args);
    }

}
