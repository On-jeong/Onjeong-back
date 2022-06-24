package com.example.onjeong.quesiton.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AnswerDto {

    private String answerContent;
    private String userName;
    private LocalDateTime answerTime;

}
