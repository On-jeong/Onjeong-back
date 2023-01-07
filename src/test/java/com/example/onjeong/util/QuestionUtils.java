package com.example.onjeong.util;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

public class QuestionUtils {

    public static Question getRandomQuestion(Family family){
        final Long questionId = 50L;
        final String questionContent = RandomStringUtils.random(8, true, true);
        final LocalDateTime questionTime = LocalDateTime.now();

        return getQuestion(questionId, questionContent, questionTime, family);
    }

    public static Question getQuestion(Long questionId, String questionContent, LocalDateTime questionTime, Family family){
        return Question.builder()
                .questionId(questionId)
                .questionContent(questionContent)
                .questionTime(questionTime)
                .family(family)
                .build();
    }

}
