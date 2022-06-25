package com.example.onjeong.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerModifyRequestDto {
    private Long answerId;
    private String answerContent;
}