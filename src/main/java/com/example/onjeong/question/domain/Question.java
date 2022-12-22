package com.example.onjeong.question.domain;

import com.example.onjeong.family.domain.Family;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id", nullable = false, unique = true)
    private Long questionId;

    @Column(name="question_content", nullable = false)
    private String questionContent;

    @Column(name = "question_time", nullable = false)
    private LocalDateTime questionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answerList = new ArrayList<>();
}