package com.example.onjeong.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private String answerContent;
    private String userName;
    private LocalDateTime answerTime;

}