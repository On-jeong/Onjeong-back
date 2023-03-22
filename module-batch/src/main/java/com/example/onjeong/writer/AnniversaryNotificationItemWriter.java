package com.example.onjeong.writer;

import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.notification.repository.NotificationRepository;
import com.example.onjeong.question.domain.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AnniversaryNotificationItemWriter implements ItemWriter<List<Notifications>> {

    private final NotificationRepository notificationRepository;

    @Override
    public void write(List<? extends List<Notifications>> items) throws Exception {
        for(List<Notifications> item : items){
            notificationRepository.saveAll(item);
        }
    }
}
