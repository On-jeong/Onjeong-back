package com.example.onjeong.job;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.config.BatchConfig;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.processor.AnniversaryNotificationProcessor;
import com.example.onjeong.writer.AnniversaryNotificationItemWriter;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class AnniversaryNotificationJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final BatchConfig batchConfig;

    private final AnniversaryNotificationProcessor anniversaryNotificationProcessor;
    private final AnniversaryNotificationItemWriter anniversaryNotificationItemWriter;

    @Bean
    public Job notificationBuildJob() {
        return jobBuilderFactory.get("notificationBuildJob")
                .start(notificationBuildStep())
                .build();
    }

    @Bean
    public Step notificationBuildStep() {
        return stepBuilderFactory.get("notificationBuildStep")
                .<Anniversary, List<Notifications>>chunk(batchConfig.getChunk())
                .reader(anniversaryReader())
                .processor(anniversaryNotificationProcessor)
                .writer(anniversaryNotificationItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Anniversary> anniversaryReader() {

        HashMap<String, Object> paramValues = new HashMap<>();
        paramValues.put("date", LocalDate.now().plusDays(1));

        return new JpaPagingItemReaderBuilder<Anniversary>()
                .name("anniversaryReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(batchConfig.getChunk())
                .queryString("select a from Anniversary a WHERE a.anniversaryDate = :date and a.anniversaryType = 'ANNIVERSARY'")
                .parameterValues(paramValues)
                .build();
    }
}
