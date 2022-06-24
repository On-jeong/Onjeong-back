package com.example.onjeong.mail.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MailDto {
    private Long mailId;
    private String mailContent;
    private String sendUserName;
    private String receiveUserName;
    private boolean checkRead;
    private LocalDateTime sendTime;
}
