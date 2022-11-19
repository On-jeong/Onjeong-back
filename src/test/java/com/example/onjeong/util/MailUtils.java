package com.example.onjeong.util;

import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

public class MailUtils {

    public static Mail getRandomMail(User sendUser, User receiveUser){
        final Long mailId= 50L;
        final String mailContent= RandomStringUtils.random(8, true, true);
        final LocalDateTime sendTime= LocalDateTime.now();
        final boolean checkRead= true;
        final boolean senderWantDelete= true;
        final boolean receiverWantDelete= true;
        return getMail(mailId, mailContent, sendTime, checkRead, senderWantDelete, receiverWantDelete, sendUser, receiveUser);
    }


    public static Mail getMail(Long mailId, String mailContent, LocalDateTime sendTime, boolean checkRead,
                                   boolean senderWantDelete, boolean receiverWantDelete, User sendUser, User receiveUser){
        return Mail.builder()
                .mailId(mailId)
                .mailContent(mailContent)
                .sendTime(sendTime)
                .checkRead(checkRead)
                .senderWantDelete(senderWantDelete)
                .receiverWantDelete(receiverWantDelete)
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .build();
    }
}
