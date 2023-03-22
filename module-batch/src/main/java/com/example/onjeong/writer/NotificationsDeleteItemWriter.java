package com.example.onjeong.writer;

import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationsDeleteItemWriter implements ItemWriter<Notifications> {

    private final NotificationRepository notificationRepository;

    @Override
    public void write(List<? extends Notifications> items) throws Exception {
        notificationRepository.deleteAll(items);
    }
}
