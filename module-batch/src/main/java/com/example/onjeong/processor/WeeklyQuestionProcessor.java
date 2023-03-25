package com.example.onjeong.processor;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.service.QuestionBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class WeeklyQuestionProcessor implements ItemProcessor<Family, Question> {

    private final QuestionBatchService questionBatchService;
    private String questionContent = "";

    @Override
    public Question process(Family family) throws Exception {

        // 첫 시도에만 questionContent 받아오기
        if(questionContent == ""){
            questionContent = questionBatchService.findPureQuestionContent();
        }
        
        Question question = Question.builder()
                .questionTime(LocalDateTime.now())
                .questionContent(questionContent)
                .family(family)
                .build();
        return question;
    }
}