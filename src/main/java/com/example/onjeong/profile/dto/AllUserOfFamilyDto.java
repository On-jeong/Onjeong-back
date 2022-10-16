package com.example.onjeong.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllUserOfFamilyDto {
    private  Long userId;
    private String userStatus;
}
