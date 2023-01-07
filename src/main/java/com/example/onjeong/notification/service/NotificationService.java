package com.example.onjeong.notification.service;


import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.repository.AnniversaryRepository;
import com.example.onjeong.board.domain.Board;
import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.notification.domain.Notifications;
import com.example.onjeong.notification.dto.NotificationDto;
import com.example.onjeong.notification.repository.NotificationRepository;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    // 배치 추가 및 질문 시 전체 알림 추가 필요
    private final UserRepository userRepository;
    private final AnniversaryRepository anniversaryRepository;
    private final NotificationRepository notificationRepository;
    private final AuthUtil authUtil;

    @Transactional
    public void registerToken(String token) throws FirebaseMessagingException {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));

        user.updateDeviceToken(token);
    }

    @Transactional
    public void deleteToken() throws FirebaseMessagingException {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserNickname(authentication.getName())
                .orElseThrow(()-> new UserNotExistException("login user not exist", ErrorCode.USER_NOTEXIST));
        user.updateDeviceToken("");
    }

    public List<NotificationDto> getNotifications(){
        User user = authUtil.getUserByAuthentication();
        List<Notifications> notificationsList = notificationRepository.findAllByUserId(user.getUserId());
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for(Notifications n : notificationsList){
            notificationDtoList.add(NotificationDto.builder()
                    .notificationContent(n.getNotificationContent())
                    .build());
        }
        return notificationDtoList;
    }


    public void sendMail(Mail mail) throws FirebaseMessagingException {
        // 메일 전송 시 받는 사람에게 알림 전송
        String token = mail.getReceiveUser().getDeviceToken();
        String content = mail.getSendUser().getUserStatus() + "로부터 메일이 도착했습니다.";

        sendPersonalAlarm(content, token);
        saveNotifications(content, mail.getReceiveUser());
    }

    public void sendFamilyCheck(Answer answer) throws FirebaseMessagingException {
        // 한 명만 안썼으면 그 한명에게 알림 전송
        Question question = answer.getQuestion();
        List<Answer> answers = question.getAnswerList();
        Family family = answer.getUser().getFamily();
        
        List<User> answeredFamily = new ArrayList<>();
        List<User> notAnsweredFamily = new ArrayList<>();
        for(Answer a : answers){
            if(!answeredFamily.contains(a.getUser())) answeredFamily.add(a.getUser());
        }
        for(User u : family.getUsers()){
            if(!answeredFamily.contains(u)) notAnsweredFamily.add(u);
        }
        
        if(notAnsweredFamily.size() == 1){
            // 메일 수신 알림
            String token = notAnsweredFamily.get(0).getDeviceToken();
            String content = notAnsweredFamily.get(0).getUserStatus() + "님만 답변하면 가족 전체의 답변이 완성됩니다";

            // 알림 범위 논의 필요
            sendPersonalAlarm(content, token);
            saveNotifications(content, notAnsweredFamily.get(0));
        }
    }

    public void sendBoard(Board board) throws FirebaseMessagingException {

        // 오늘의 기록 작성했을 때 알림
        String topic = board.getFamily().getFamilyId().toString();
        String content = board.getUser().getUserStatus() + "님이 오늘의 기록을 작성했습니다.";

        //알림 전송
        sendFamilyAlarm(content, topic);
        for(User u : board.getFamily().getUsers()){
            if(!u.equals(board.getUser())) saveNotifications(content, u);
        }
    }

    public void sendAnswer(Answer answer) throws FirebaseMessagingException {
        // 이주의 문답 답변 작성했을 때 알림
        String topic = answer.getUser().getFamily().getFamilyId().toString();
        String content = answer.getUser().getUserStatus() + "님이 이주의 문답에 대한 답변을 작성했습니다.";

        sendFamilyAlarm(content, topic);
        for(User u : answer.getUser().getFamily().getUsers()){
            if(!u.equals(answer.getUser())) saveNotifications(content, u);
        }
    }

    public void sendProfileModify(Profile profile) throws FirebaseMessagingException {
        // 프로필이 수정될 때 알림
        String topic = profile.getFamily().getFamilyId().toString();
        String content = profile.getUser().getUserStatus() + "의 프로필이 수정되었습니다.";

        sendFamilyAlarm(content, topic);
        for(User u : profile.getFamily().getUsers()){
            saveNotifications(content, u);
        }
    }

    public void sendAnnivarsary() throws FirebaseMessagingException {
        // 기념일 전날 가족들에게 알림
        // 배치 추가 예정
        LocalDate date = LocalDate.now().plusDays(1);
        List<Anniversary> anniversaryList = anniversaryRepository.findAllByAnniversaryDate(date);

        for (Anniversary a : anniversaryList) {
            String topic = a.getFamily().getFamilyId().toString();
            String content = a.getAnniversaryContent() + "이 하루 전입니다.";

            sendFamilyAlarm(content, topic);
            for(User u : a.getFamily().getUsers()){
                saveNotifications(content, u);
            }
        }
    }


    public void sendPersonalAlarm(String content, String token) throws FirebaseMessagingException {
        try{
            Message message = Message.builder()
                    .putData("time", LocalDateTime.now().toString())
                    .setNotification(new Notification("On:Jeong", content))
                    .setToken(token)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("메시지 전송 알림 완료 : " + response);
        } catch (Exception e){} // 에러가 발생해도 무시하고 다음 코드 진행
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

    @Transactional
    public void saveNotifications(String content, User user){
        Notifications notification = Notifications.builder()
                .notificationContent(content)
                .notificationTime(LocalDateTime.now())
                .user(user)
                .build();
        notificationRepository.save(notification);
    }
}
