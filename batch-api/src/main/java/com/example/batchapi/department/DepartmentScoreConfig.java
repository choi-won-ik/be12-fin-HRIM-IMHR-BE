package com.example.batchapi.department;

import com.example.batchapi.department.model.Department;
import com.example.batchapi.department.model.DepartmentScore;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DepartmentScoreConfig {

    private final DepartmentRepository departmentRepository;
    private final EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public ItemReader<Department> scoreReader() {
        System.out.println("reader 실행");
        return new RepositoryItemReaderBuilder<Department>()
                .repository(departmentRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("idx", Sort.Direction.ASC))
                .name("rankReader")
                .build();
    }

    @Bean
    public ItemWriter<DepartmentScore> scoreWriter() {
        System.out.println("writer 실행");
        return new JpaItemWriterBuilder<DepartmentScore>()
                .entityManagerFactory(entityManagerFactory)
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
    public Step scoreStep(
            JobRepository jobRepository,
            ItemReader<Department> scoreReader,
            ItemProcessor<Department, DepartmentScore> scoreProcessor,
            ItemWriter<DepartmentScore> scoreWriter) {
        return new StepBuilder("scoreStep", jobRepository)
                .<Department, DepartmentScore>chunk(30, jpaTransactionManager(entityManagerFactory))
                .reader(scoreReader)
                .processor(scoreProcessor) // @Component로 등록된 RankProcessor 자동 주입
                .writer(scoreWriter)
                .transactionManager(jpaTransactionManager(entityManagerFactory))
                .build();
    }
}
