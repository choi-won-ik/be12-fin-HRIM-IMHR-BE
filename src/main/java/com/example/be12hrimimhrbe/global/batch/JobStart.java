package com.example.be12hrimimhrbe.global.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobStart {

    @Bean
    public Job rankJob(JobRepository jobRepository, Step scoreStep, Step rankStep, Step memberStep) {
        return new JobBuilder("rankJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(scoreStep)
                .on("COMPLETED").to(rankStep)
                .from(scoreStep)
                .on("*").end() // scoreStep 실패 시 Job 종료

                .from(rankStep)
                .on("COMPLETED").to(memberStep)
                .from(rankStep)
                .on("*").end() // rankStep 실패 시 Job 종료

                .end()
                .build();
    }
}
