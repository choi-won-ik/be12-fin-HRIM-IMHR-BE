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
    @Scheduled(cron = "0 5,10,15,20,25,30,35,40,45,50,55 * * * *", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {

        log.info("first schedule start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        rankService.batch();
        rankService.batch1();
        rankService.batch2();
    }
}