package com.example.onjeong.repository;

import com.example.onjeong.domain.Answer;
import com.example.onjeong.domain.Mail;
import com.example.onjeong.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM Question q WHERE q.family_id = id " +
                    "ORDER BY q.question_time limit 1")
    Question findWeeklyQuestion(Long id);
}
