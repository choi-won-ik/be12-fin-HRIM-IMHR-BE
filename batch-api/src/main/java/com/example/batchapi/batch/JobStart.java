package com.example.batchapi.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class JobStart implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;
    @Value("${spring.batch.job.name}")
    private String jobName;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("배치 작업 시작");
        try {
            Job job = (Job) applicationContext.getBean(jobName);
            System.out.println(job);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
            System.out.println("배치 작업 성공");
        } catch (Exception e) {
            System.out.println("배치 작업 실패");
            e.printStackTrace();
            // Optional: System.exit(1); 대신 예외 그대로 throw
            throw e;
        }
    }
}
