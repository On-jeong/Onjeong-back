package com.example.onjeong.question;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.QuestionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 질문_여러개_삭제(){
        //given
        final Family family= FamilyUtils.getRandomFamily();
        final List<Question> questions= new ArrayList<>();

        for(int i=0;i<3;i++){
            final Question question= QuestionUtils.getRandomQuestion(family);
            questions.add(question);
        }
        questionRepository.saveAll(questions);


        //when
        questionRepository.deleteAllByFamily(family);


        //then
    }
}
