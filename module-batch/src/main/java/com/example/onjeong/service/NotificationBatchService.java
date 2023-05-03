package com.example.onjeong.service;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.user.domain.User;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationBatchService {

    public List<Notifications> sendAnniversary(Anniversary a) throws FirebaseMessagingException {
        List<String> tokens = new ArrayList<>();
        String content = a.getAnniversaryContent() + "이 하루 전입니다.";

        List<Notifications> notificationList = new ArrayList<>();
        for(User u : a.getFamily().getUsers()){
            if(u.getDeviceToken() != null) topics.add(u.getDeviceToken());
            Notifications notification = Notifications.builder()
                    .notificationContent(content)
                    .notificationTime(LocalDateTime.now())
                    .user(u)
                    .build();
            notificationList.add(notification);
        }
        sendFamilyAlarm(content, tokens);
        return notificationList;
    }

    public void sendFamilyAlarm(String content, List<String> tokens) throws FirebaseMessagingException {
        try{
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .putData("time", LocalDateTime.now().toString())
                    .setNotification(new Notification("OnJeong", content))
                    .addAllTokens(tokens)
                    .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
            log.info("메시지 전송 알림 완료 : " + response);
        } catch (Exception e){} // 에러가 발생해도 무시하고 다음 코드 진행
    }

}
