package com.example.onjeong.repository;

import com.example.onjeong.domain.Answer;
import com.example.onjeong.domain.Mail;
import com.example.onjeong.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);

}
