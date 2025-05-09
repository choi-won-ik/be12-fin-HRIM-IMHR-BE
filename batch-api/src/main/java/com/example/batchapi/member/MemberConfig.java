package com.example.batchapi.member;


import com.example.batchapi.member.model.Member;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
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
public class MemberConfig {

    private final MemberRepository memberRepository;
    private final EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public ItemReader<Member> memberReader() {
        System.out.println("reader 실행");
        return new RepositoryItemReaderBuilder<Member>()
                .repository(memberRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("idx", Sort.Direction.ASC))
                .name("rankReader")
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> memberProcessor() {
        System.out.println("processor 실행");
        return member -> {
            member.setEScore(0);
            member.setSScore(0);
            member.setGScore(0);
            return member;
        };
    }

    @Bean
    public ItemWriter<Member> memberWriter() {
        System.out.println("writer 실행");
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step memberStep(
            JobRepository jobRepository,
            ItemReader<Member> memberReader,
            ItemProcessor<Member, Member> memberProcessor,
            ItemWriter<Member> memberWriter) {
        return new StepBuilder("memberStep", jobRepository)
                .<Member, Member>chunk(30, jpaTransactionManager(entityManagerFactory))
                .reader(memberReader)
                .processor(memberProcessor) // @Component로 등록된 RankProcessor 자동 주입
                .writer(memberWriter)
                .transactionManager(jpaTransactionManager(entityManagerFactory))
                .build();
    }



}
