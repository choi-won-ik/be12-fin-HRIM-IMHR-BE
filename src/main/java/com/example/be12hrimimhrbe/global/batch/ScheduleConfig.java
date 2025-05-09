package com.example.be12hrimimhrbe.global.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    // 매 시간 5분, 10분, 15분 ... 55분마다 실행 (5분 간격)
    @Scheduled(cron = "0 5,10,15,20,25,30,35,40,45,50,55 * * * *", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {
        log.info("rankJob schedule triggered");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("rankJob"), jobParameters);
    }
}