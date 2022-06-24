package com.example.onjeong.quesiton.domain;

import com.example.onjeong.user.domain.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Answer {
    @Id
    @GeneratedValue
    @Column(name="answer_id")
    private Long answerId;

    @Column(name="answer_content")
    private String answerContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "answer_time")
    private LocalDateTime answerTime;

    public void updateContent(String content) {
        this.answerContent = content;
    }
}
