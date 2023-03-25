package com.example.onjeong.job;

import com.example.onjeong.config.BatchConfig;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.writer.NotificationsDeleteItemWriter;
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
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class NotificationDeleteJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final BatchConfig batchConfig;

    private final NotificationsDeleteItemWriter notificationsDeleteItemWriter;

    @Bean
    public Job notificationsDeleteJob() {
        return jobBuilderFactory.get("notificationsDeleteJob")
                .start(notificationsDeleteStep())
                .build();
    }

    @Bean
    public Step notificationsDeleteStep() {
        return stepBuilderFactory.get("notificationsDeleteStep")
                .<Notifications, Notifications>chunk(batchConfig.getChunk())
                .reader(notificationsReader())
                .writer(notificationsDeleteItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Notifications> notificationsReader() {
        HashMap<String, Object> paramValues = new HashMap<>();
        paramValues.put("date", LocalDateTime.now().minusDays(4));

        return new JpaPagingItemReaderBuilder<Notifications>()
                .name("notificationsReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(batchConfig.getChunk())
                .queryString("select n from Notifications n WHERE n.notificationTime <= :date")
                .parameterValues(paramValues)
                .build();
    }
}
