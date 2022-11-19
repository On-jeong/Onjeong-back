package com.example.onjeong.question;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.AnswerUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.QuestionUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;


    @Test
    void 답변_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final User user= UserUtils.getRandomUser(family);
        final Question question= QuestionUtils.getRandomQuestion(family);

        final List<Answer> answers= new ArrayList<>();
        for(int i=0;i<3;i++){
            final Answer answer= AnswerUtils.getRandomAnswer(user, question);
            answers.add(answer);
        }
        answerRepository.saveAll(answers);


        //when
        answerRepository.deleteAllByUser(user);


        //then
        assertThat(answerRepository.findAllByUser(user).size()).isEqualTo(0);
    }
}
