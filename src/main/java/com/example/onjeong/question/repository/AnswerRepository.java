package com.example.onjeong.question.repository;

import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);
    void deleteAllByUser(User user);
}