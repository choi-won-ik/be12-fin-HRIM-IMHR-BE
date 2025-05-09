package com.example.be12hrimimhrbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableMongoRepositories
@SpringBootApplication
//@EnableScheduling // 스케줄링 어노테이션 활성화 설정
public class Be12HrimImhrBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Be12HrimImhrBeApplication.class, args);
	}

}
