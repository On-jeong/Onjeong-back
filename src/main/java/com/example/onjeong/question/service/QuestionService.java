package com.example.onjeong.question.service;


import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.dto.AnswerDto;
import com.example.onjeong.question.dto.AnswerModifyRequestDto;
import com.example.onjeong.question.dto.AnswerRequestDto;
import com.example.onjeong.question.dto.QuestionDto;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final FamilyRepository familyRepository;

    // 관리자용 질문 등록
    @Transactional
    public Boolean registerQuestion(String questionContent){
        List<Family> families = familyRepository.findAll();
        List<Question> questionList = new ArrayList<>();

        for(Family f : families){
            Question q = Question.builder()
                .questionContent(questionContent)
                .questionTime(LocalDateTime.now())
                .family(f)
                .build();
            questionList.add(q);
        }

        questionRepository.saveAll(questionList);

        return true;
    }

    public QuestionDto showQuestion(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());

        Question question = questionRepository.findWeeklyQuestion(user.get().getFamily().getFamilyId());
        QuestionDto questionDto = QuestionDto.builder()
                .questonId(question.getQuestionId())
                .questionContent(question.getQuestionContent())
                .build();
        return questionDto;
    }

    public List<AnswerDto> showAllAnswer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());

        Question question = questionRepository.findWeeklyQuestion(user.get().getFamily().getFamilyId());

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

    public List<String> showAllAnswerFamily(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());

        Question question = questionRepository.findWeeklyQuestion(user.get().getFamily().getFamilyId());

        List<String> answeredFamily = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestion(question);

        for(Answer a : answers){
            answeredFamily.add(a.getUser().getUserStatus());
        }

        return answeredFamily;
    }

    @Transactional
    public AnswerDto registerAnswer(String answerContent){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserNickname(authentication.getName());
        Question question = questionRepository.findWeeklyQuestion(user.get().getFamily().getFamilyId());

        Answer answer = Answer.builder()
                .question(question)
                .user(user.get())
                .answerContent(answerContent)
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
