package com.example.onjeong.question.repository;


import com.example.onjeong.family.domain.Family;
import com.example.onjeong.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM question q WHERE q.family_id = :id " +
                    "ORDER BY q.question_time DESC limit 1")
    Question findWeeklyQuestion(@Param("id") Long id);

    Page<Question> findAllByFamily(Pageable pageable, Family family);
}