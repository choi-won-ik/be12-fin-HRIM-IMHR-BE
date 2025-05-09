package com.example.be12hrimimhrbe.global.batch;


import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.rank.Rank;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
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
import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class RankConfig {

    private final RankRepository rankRepository;
    private final CompanyRepository companyRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public ItemReader<Company> rankReader() {
        System.out.println("reader 실행");
        return new RepositoryItemReaderBuilder<Company>()
                .repository(companyRepository)
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
    public ItemWriter<List<Rank>> delegatingRankWriter(ItemWriter<Rank> rankWriter) {
        return items -> {
            List<Rank> flattened = items.getItems().stream()
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .toList();

            if (!flattened.isEmpty()) {
                // ✅ List<Rank> → Chunk<Rank>로 변환
                Chunk<Rank> chunk = new Chunk<>(flattened);
                rankWriter.write(chunk);
            } else {
                System.out.println("저장할 Rank 항목이 없습니다.");
            }
        };
    }


    @Bean
    public Step rankStep(
            JobRepository jobRepository,
            ItemReader<Company> rankReader,
            ItemProcessor<Company, List<Rank>> rankProcessor,
            ItemWriter<Rank> rankWriter,
            EntityManagerFactory entityManagerFactory
    ) {
        return new StepBuilder("rankStep", jobRepository)
                .<Company, List<Rank>>chunk(30, jpaTransactionManager(entityManagerFactory))
                .reader(rankReader)
                .processor(rankProcessor)
                .writer(delegatingRankWriter(rankWriter))
                .transactionManager(jpaTransactionManager(entityManagerFactory))
                .build();
    }

}