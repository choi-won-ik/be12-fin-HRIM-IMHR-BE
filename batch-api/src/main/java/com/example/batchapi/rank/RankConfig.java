package com.example.batchapi.rank;

import com.example.batchapi.rank.model.Rank;
import com.example.batchapi.repository.RankRepository;
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

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RankConfig {

    private final RankRepository rankRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public ItemReader<Rank> rankReader() {
        System.out.println("reader 실행");
        return new RepositoryItemReaderBuilder<Rank>()
                .repository(rankRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("idx", Sort.Direction.ASC))
                .name("rankReader")
                .build();
    }

    @Bean
    public ItemWriter<Rank> rankWriter() {
        System.out.println("writer 실행");
        return new JpaItemWriterBuilder<Rank>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step rankStep(
            JobRepository jobRepository,
            ItemReader<Rank> rankReader,
            ItemProcessor<Rank, Rank> rankProcessor,
            ItemWriter<Rank> rankWriter
    ) {
        return new StepBuilder("rankStep", jobRepository)
                .<Rank, Rank>chunk(30, jpaTransactionManager(entityManagerFactory))
                .reader(rankReader)
                .processor(rankProcessor) // @Component로 등록된 RankProcessor 자동 주입
                .writer(rankWriter)
                .transactionManager(jpaTransactionManager(entityManagerFactory))
                .build();
    }
}