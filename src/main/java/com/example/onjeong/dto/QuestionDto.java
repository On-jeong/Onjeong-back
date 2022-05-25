package com.example.onjeong.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuestionDto {
    private Long questonId;
    private String questionContent;
}
