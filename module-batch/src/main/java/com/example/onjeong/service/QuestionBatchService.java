package com.example.onjeong.service;


import com.example.onjeong.question.domain.PureQuestion;
import com.example.onjeong.question.repository.PureQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionBatchService {

    private final PureQuestionRepository pureQuestionRepository;

    public String findPureQuestionContent(){
        PureQuestion pureQuestion = pureQuestionRepository.chooseWeeklyQuestion();
        String pureQuestionContent = pureQuestion.getPureQuestionContent();
        pureQuestionRepository.delete(pureQuestion);
        return pureQuestionContent;
    }
}
