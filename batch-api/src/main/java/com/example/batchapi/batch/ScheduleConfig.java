package com.example.batchapi.batch;

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

//    // 매 시간 정각
//    @Scheduled(cron = "0 32 * * * *", zone = "Asia/Seoul")
    // 매월 첫 쨰주 월용일 00:00:10
    @Scheduled(cron = "10 0 0 ? * 2#1", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {

        log.info("first schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        // "firstJob" : 2-1 Job(작업 정의)에서 Bean으로 정의한 Job의 이름
        jobLauncher.run(jobRegistry.getJob("rankJob"), jobParameters);
    }
}