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
public class UserInformationsGetDto {
    private List<FavoriteGetDto> favorites;
    private List<HateGetDto> hates;
    private List<InterestGetDto> interests;
    private List<ExpressionGetDto> expressions;
}
