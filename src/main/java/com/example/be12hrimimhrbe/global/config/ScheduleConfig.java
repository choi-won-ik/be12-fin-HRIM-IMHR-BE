package com.example.be12hrimimhrbe.global.config;

import com.example.be12hrimimhrbe.domain.rank.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {
    private final RankService rankService;
//    // 매 시간 정각
//    @Scheduled(cron = "0 32 * * * *", zone = "Asia/Seoul")
    // 매월 첫 쨰주 월용일 00:00:10
    @Scheduled(cron = "0 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58 * * * *", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {

        log.info("first schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        rankService.batch();
        rankService.batch1();
        rankService.batch2();
        // "firstJob" : 2-1 Job(작업 정의)에서 Bean으로 정의한 Job의 이름

    }
}