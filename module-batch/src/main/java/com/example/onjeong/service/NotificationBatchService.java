package com.example.onjeong.service;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.user.domain.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationBatchService {

    public List<Notifications> sendAnniversary(Anniversary a) throws FirebaseMessagingException {
        String topic = a.getFamily().getFamilyId().toString();
        String content = a.getAnniversaryContent() + "이 하루 전입니다.";

        sendFamilyAlarm(content, topic);

        List<Notifications> notificationList = new ArrayList<>();
        for(User u : a.getFamily().getUsers()){
            Notifications notification = Notifications.builder()
                    .notificationContent(content)
                    .notificationTime(LocalDateTime.now())
                    .user(u)
                    .build();
            notificationList.add(notification);
        }
        return notificationList;
    }

    public void sendFamilyAlarm(String content, String topic) throws FirebaseMessagingException {
        try{
            Message message = Message.builder()
                    .putData("time", LocalDateTime.now().toString())
                    .setNotification(new Notification("OnJeong", content))
                    .setTopic(topic)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("메시지 전송 알림 완료 : " + response);
        } catch (Exception e){} // 에러가 발생해도 무시하고 다음 코드 진행
    }

}
