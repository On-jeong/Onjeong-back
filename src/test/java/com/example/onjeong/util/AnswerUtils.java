package com.example.onjeong.util;

import com.example.onjeong.board.domain.Board;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnswerUtils {

    public static Answer getRandomAnswer(User user, Question question){
        final Long answerId= 50L;
        final String answerContent= RandomStringUtils.random(8, true, true);
        final LocalDateTime answerTime= LocalDateTime.now();
        return getAnswer(answerId, answerContent, answerTime, user, question);
    }


    public static Answer getAnswer(Long answerId, String answerContent, LocalDateTime answerTime,
                                   User user, Question question){
        return Answer.builder()
                .answerId(answerId)
                .answerContent(answerContent)
                .answerTime(answerTime)
                .user(user)
                .question(question)
                .build();
    }
}
