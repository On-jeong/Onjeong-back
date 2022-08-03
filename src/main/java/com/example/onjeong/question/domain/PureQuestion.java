package com.example.onjeong.question.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PureQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pure_question_id", nullable = false, unique = true)
    private Long PureQuestionId;

    @Column(name="pure_question_content", nullable = false)
    private String PureQuestionContent;
}
