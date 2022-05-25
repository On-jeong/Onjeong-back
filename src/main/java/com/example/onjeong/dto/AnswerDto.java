package com.example.onjeong.dto;

import com.example.onjeong.domain.Question;
import com.example.onjeong.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
public class AnswerDto {

    private String answerContent;
    private String userName;
    private LocalDateTime answerTime;

}
