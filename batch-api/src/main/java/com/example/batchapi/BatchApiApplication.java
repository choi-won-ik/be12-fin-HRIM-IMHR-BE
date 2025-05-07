package com.example.batchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 어노테이션 활성화 설정
public class BatchApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApiApplication.class, args);
    }

}
