package com.example.onjeong.quesiton.repository;

import com.example.onjeong.quesiton.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM Question q WHERE q.family_id = id " +
                    "ORDER BY q.question_time limit 1")
    Question findWeeklyQuestion(Long id);
}
