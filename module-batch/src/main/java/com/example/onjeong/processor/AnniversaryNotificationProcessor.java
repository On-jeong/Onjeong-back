package com.example.onjeong.processor;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.service.NotificationBatchService;
import com.example.onjeong.service.QuestionBatchService;
import com.example.onjeong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class AnniversaryNotificationProcessor implements ItemProcessor<Anniversary, List<Notifications>> {

    private final NotificationBatchService notificationBatchService;

    @Override
    public List<Notifications> process(Anniversary item) throws Exception {
        return notificationBatchService.sendAnniversary(item);
    }
}
