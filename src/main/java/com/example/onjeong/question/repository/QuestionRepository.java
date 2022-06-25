package com.example.onjeong.question.repository;


import com.example.onjeong.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM question q WHERE q.family_id = :id " +
                    "ORDER BY q.question_time DESC limit 1")
    Question findWeeklyQuestion(@Param("id") Long id);
}