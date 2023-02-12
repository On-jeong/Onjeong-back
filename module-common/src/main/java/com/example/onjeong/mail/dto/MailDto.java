package com.example.onjeong.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {
    private Long mailId;
    private String mailContent;
    private String sendUserName;
    private String receiveUserName;
    private Boolean checkRead;
    private LocalDateTime sendTime;
}