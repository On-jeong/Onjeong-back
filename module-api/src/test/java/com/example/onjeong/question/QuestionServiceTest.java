package com.example.onjeong.question;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.dto.AnswerDto;
import com.example.onjeong.question.dto.AnswerModifyRequestDto;
import com.example.onjeong.question.dto.QuestionDto;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.question.service.QuestionService;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.AuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private Pageable pageable;

    @Test
    void 질문조회(){
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final List<Question> questionList = new ArrayList<>();
        for(int i=0; i<3; i++){
            questionList.add(QuestionUtils.getRandomQuestion(family));
        }
        final Page<Question> questions = new PageImpl(questionList);
        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(questions).when(questionRepository).findAllByFamily(pageable, family);

        //when
        List<QuestionDto> questionDtos = questionService.showQuestion(pageable);

        //then
        assertEquals(questionDtos.size(), 3);
    }

    @Test
    void 답변조회(){
        //given
        final Long questionId = 1L;
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final Question question = QuestionUtils.getRandomQuestion(family);

        List<Answer> answerList = new ArrayList<>();
        for(int i=0; i<3; i++){
            answerList.add(AnswerUtils.getRandomAnswer(user, question));
        }
        
        doReturn(Optional.of(question)).when(questionRepository).findById(questionId);
        doReturn(answerList).when(answerRepository).findByQuestion(question);

        //when
        List<AnswerDto> answerDtoList = questionService.showAllAnswer(questionId);

        //then
        assertEquals(answerDtoList.size(), 3);
    }

    @Nested
    class 답변한가족확인{
        @Test
        void 답변한가족_목록확인(){
            //given
            final Family family = FamilyUtils.getRandomFamily();
            final User user1 = UserUtils.getRandomUser(family);
            final User user2 = UserUtils.getRandomUser(family);
            final Question question = QuestionUtils.getRandomQuestion(family);
            final Answer answer1 = AnswerUtils.getRandomAnswer(user1, question);

            List<Answer> answerList = new ArrayList<>();
            answerList.add(answer1);

            doReturn(user2).when(authUtil).getUserByAuthentication();
            doReturn(question).when(questionRepository).findWeeklyQuestion(family.getFamilyId());
            doReturn(answerList).when(answerRepository).findByQuestion(question);
            //when
            List<String> answerFamilyList = questionService.showAllAnswerFamily();

            //then
            assertEquals(answerFamilyList.size(),1);
        }

        @Test
        @DisplayName("가족 일부만 답변했을 때 모두 답변했는지 여부가 False로 반환되는지")
        void 모두답변했는지_여부확인1(){
            //given
            final Family family = FamilyUtils.getRandomFamily();
            final User user1 = UserUtils.getRandomUser(family);
            final User user2 = UserUtils.getRandomUser(family);
            family.getUsers().add(user1);
            family.getUsers().add(user2);

            final Question question = QuestionUtils.getRandomQuestion(family);
            final Answer answer1 = AnswerUtils.getRandomAnswer(user1, question);

            List<Answer> answerList = new ArrayList<>();
            answerList.add(answer1);

            doReturn(user2).when(authUtil).getUserByAuthentication();
            doReturn(question).when(questionRepository).findWeeklyQuestion(family.getFamilyId());
            doReturn(answerList).when(answerRepository).findByQuestion(question);

            //when
            Boolean result = questionService.answerFamilyCheck();

            //then
            assertFalse(result);
        }

        @Test
        @DisplayName("가족 전체가 답변했을 때 모두 답변했는지 여부가 True로 반환되는지")
        void 모두답변했는지_여부확인2(){
            //given
            final Family family = FamilyUtils.getRandomFamily();
            final User user1 = UserUtils.getRandomUser(family);
            final User user2 = UserUtils.getRandomUser(family);
            family.getUsers().add(user1);
            family.getUsers().add(user2);

            final Question question = QuestionUtils.getRandomQuestion(family);
            final Answer answer1 = AnswerUtils.getRandomAnswer(user1, question);
            final Answer answer2 = AnswerUtils.getRandomAnswer(user2, question);

            List<Answer> answerList = new ArrayList<>();
            answerList.add(answer1);
            answerList.add(answer2);

            doReturn(user2).when(authUtil).getUserByAuthentication();
            doReturn(question).when(questionRepository).findWeeklyQuestion(family.getFamilyId());
            doReturn(answerList).when(answerRepository).findByQuestion(question);

            //when
            Boolean result = questionService.answerFamilyCheck();

            //then
            assertTrue(result);
        }
    }


    @Test
    void 답변작성(){
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final Question question = QuestionUtils.getRandomQuestion(family);

        final String answerContent = "answer";

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(question).when(questionRepository).findWeeklyQuestion(family.getFamilyId());
        //when
        Answer result = questionService.registerAnswer(answerContent);

        //then
        assertEquals(answerContent, result.getAnswerContent());
        verify(answerRepository,times(1)).save(any(Answer.class));
    }

    @Test
    void 답변수정(){
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final Question question = QuestionUtils.getRandomQuestion(family);
        final Answer answer = AnswerUtils.getRandomAnswer(user, question);
        final AnswerModifyRequestDto answerModifyRequestDto = AnswerModifyRequestDto.builder()
                .answerId(1L)
                .answerContent("modify")
                .build();

        doReturn(Optional.of(answer)).when(answerRepository).findById(answerModifyRequestDto.getAnswerId());

        //when
        AnswerDto result = questionService.modifyAnswer(answerModifyRequestDto);

        //then
        assertEquals(answerModifyRequestDto.getAnswerContent(), result.getAnswerContent());
    }

    @Test
    void 답변삭제(){
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final Question question = QuestionUtils.getRandomQuestion(family);
        final Answer answer = AnswerUtils.getRandomAnswer(user, question);
        final Long answerId = 1L;

        doReturn(Optional.of(answer)).when(answerRepository).findById(answerId);

        //when
        questionService.deleteAnswer(answerId);

        //then
        verify(answerRepository,times(1)).delete(any(Answer.class));
    }

}