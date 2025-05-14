package com.example.batchapi.batch;

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
    public Job rankJob(JobRepository jobRepository, Step rankStep) {
        return new JobBuilder("rankJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(rankStep)
                .build();
    }

    @Bean
    public Job scoreJob(JobRepository jobRepository, Step scoreStep) {
        return new JobBuilder("scoreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(scoreStep)
                .build();
    }

    @Bean
    public Job memberJob(JobRepository jobRepository, Step memberStep) {
        return new JobBuilder("memberJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(memberStep)
                .build();
    }
}
