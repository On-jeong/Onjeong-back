package com.example.onjeong.question.repository;

import com.example.onjeong.question.domain.PureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PureQuestionRepository extends JpaRepository<PureQuestion, Long> {

    @Query(value="select * from pure_question order by rand() limit 1", nativeQuery = true)
    PureQuestion chooseWeeklyQuestion();
}
