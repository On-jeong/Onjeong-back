package com.example.onjeong.question;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.AnswerRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AnswerUtils;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.QuestionUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void 답변전체조회(){
        //given
        Family family = FamilyUtils.getRandomFamily();
        Question question = QuestionUtils.getRandomQuestion(family);
        User user = UserUtils.getRandomUser(family);
        Answer answer = AnswerUtils.getRandomAnswer(user, question);
        answerRepository.save(answer);

        // when
        List<Answer> answerList = answerRepository.findByQuestion(question);

        // then
        for(Answer a : answerList){
            assertEquals(a.getQuestion(), question);
        }

    }
}