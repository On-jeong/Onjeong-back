package com.example.onjeong.question.repository;

import com.example.onjeong.question.domain.PureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PureQuestionRepository extends JpaRepository<PureQuestion, Long> {

    @Query(value="select * from pure_question order by pure_question_id limit 1", nativeQuery = true)
    PureQuestion chooseWeeklyQuestion();

    @Query(value="select * from pure_question where pure_question_content = :content", nativeQuery = true)
    PureQuestion findPureQuestion(@Param("content") String content);
}
