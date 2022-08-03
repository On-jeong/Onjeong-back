package com.example.onjeong.question.repository;

import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);

}