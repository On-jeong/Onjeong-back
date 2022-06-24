package com.example.onjeong.quesiton.repository;

import com.example.onjeong.quesiton.domain.Answer;
import com.example.onjeong.quesiton.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);

}
