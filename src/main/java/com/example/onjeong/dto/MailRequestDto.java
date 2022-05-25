package com.example.onjeong.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailRequestDto {
    private Long sendUserId;
    private Long receiveUserId;
    private String mailContent;
}
