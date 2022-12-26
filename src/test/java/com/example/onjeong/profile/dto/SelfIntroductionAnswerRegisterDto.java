package com.example.onjeong.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SelfIntroductionAnswerRegisterDto {
    private String selfIntroductionAnswerContent;
}