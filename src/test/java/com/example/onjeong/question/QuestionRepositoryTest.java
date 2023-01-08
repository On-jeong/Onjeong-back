package com.example.onjeong.question;

import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void 이주의문답조회(){
        //given
        final Long familyId = 50L;

        //when
        Question question = questionRepository.findWeeklyQuestion(familyId);

        //then

    }
}