package com.example.onjeong.quesiton.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnswerRequestDto {
    private Long questionId;
    private Long userId;
    private String answerContent;
}
