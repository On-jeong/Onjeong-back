package com.example.onjeong.service;

import com.example.onjeong.domain.*;
import com.example.onjeong.dto.AnswerDto;
import com.example.onjeong.dto.AnswerModifyRequestDto;
import com.example.onjeong.dto.AnswerRequestDto;
import com.example.onjeong.dto.QuestionDto;
import com.example.onjeong.repository.AnswerRepository;
import com.example.onjeong.repository.FamilyRepository;
import com.example.onjeong.repository.QuestionRepository;
import com.example.onjeong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final FamilyRepository familyRepository;

    // 관리자용 질문 등록
    public Boolean registerQuestion(String questionContent){
        List<Family> families = familyRepository.findAll();

        for(Family f : families){
            Question q = Question.builder()
                    .questionContent(questionContent)
                    .family(f)
                    .build();

            questionRepository.save(q);
        }

        return true;
    }

    public QuestionDto showQuestion(Long familyId){
        Question question = questionRepository.findWeeklyQuestion(familyId);
        QuestionDto questionDto = QuestionDto.builder()
                .questonId(question.getQuestionId())
                .questionContent(question.getQuestionContent())
                .build();
        return questionDto;
    }

    public List<AnswerDto> showAllAnswer(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));

        List<AnswerDto> answerDtos = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestion(question);

        for(Answer a : answers){
            AnswerDto answer = AnswerDto.builder()
                    .userName(a.getUser().getUserName())
                    .answerContent(a.getAnswerContent())
                    .answerTime(a.getAnswerTime())
                    .build();
            answerDtos.add(answer);
        }

        return answerDtos;
    }

    @Transactional
    public AnswerDto registerAnswer(AnswerRequestDto answerRequestDto){

        Question question = questionRepository.findById(answerRequestDto.getQuestionId())
                 .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));

        User user = userRepository.findById(answerRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Answer answer = Answer.builder()
                .question(question)
                .user(user)
                .answerContent(answerRequestDto.getAnswerContent())
                .answerTime(LocalDateTime.now())
                .build();
        answerRepository.save(answer);

        AnswerDto answerDto = AnswerDto.builder()
                .userName(answer.getUser().getUserName())
                .answerContent(answer.getAnswerContent())
                .answerTime(answer.getAnswerTime())
                .build();

        return answerDto;
    }

    @Transactional
    public AnswerDto modifyAnswer(AnswerModifyRequestDto answerModifyRequestDto) {

        Answer answer = answerRepository.findById(answerModifyRequestDto.getAnswerId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));

        if (answerModifyRequestDto.getAnswerContent() != null) {
            answer.updateContent(answerModifyRequestDto.getAnswerContent());
        }

        AnswerDto answerDto = AnswerDto.builder()
                .userName(answer.getUser().getUserName())
                .answerContent(answer.getAnswerContent())
                .answerTime(answer.getAnswerTime())
                .build();

        return answerDto;
    }

    @Transactional
    public boolean deleteAnswer(Long answerId){

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));

        answerRepository.delete(answer);
        return true;
    }

}
