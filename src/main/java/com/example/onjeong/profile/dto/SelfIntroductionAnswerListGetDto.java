package com.example.onjeong.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelfIntroductionAnswerListGetDto {
    private List<SelfIntroductionAnswerGetDto> favorites;
    private List<SelfIntroductionAnswerGetDto> hates;
    private List<SelfIntroductionAnswerGetDto> interests;
    private List<SelfIntroductionAnswerGetDto> expressions;
}
