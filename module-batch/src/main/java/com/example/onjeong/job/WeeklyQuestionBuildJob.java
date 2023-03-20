package com.example.onjeong.job;
import com.example.onjeong.config.BatchConfig;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.processor.WeeklyQuestionProcessor;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.writer.WeeklyQuestionItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class WeeklyQuestionBuildJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final BatchConfig batchConfig;

    private final WeeklyQuestionProcessor weeklyQuestionProcessor;
    private final WeeklyQuestionItemWriter weeklyQuestionItemWriter;

    @Bean
    public Job questionBuildJob() {
        return jobBuilderFactory.get("questionBuildJob")
                .start(questionBuildStep())
                .build();
    }

    @Bean
    public Step questionBuildStep() {
        return stepBuilderFactory.get("questionBuildStep")
                .<Family, Question>chunk(batchConfig.getChunk())
                .reader(familyReader())
                .processor(weeklyQuestionProcessor)
                .writer(weeklyQuestionItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Family> familyReader() {
        return new JpaPagingItemReaderBuilder<Family>()
                .name("familyReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(batchConfig.getChunk())
                .queryString("select f from Family f")
                .build();
    }

}