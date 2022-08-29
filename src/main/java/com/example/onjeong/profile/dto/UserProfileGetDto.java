package com.example.onjeong.profile.dto;

import com.example.onjeong.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;

@Getter
@NoArgsConstructor
public class UserProfileGetDto {
    private String profileImageUrl;
    private boolean checkProfileImage;
    private String name;
    private Integer age;
    private String birth;
    private String message;

    @Builder
    public UserProfileGetDto(User user, String profileImageUrl, boolean checkProfileImage, String message) {
        this.profileImageUrl= profileImageUrl;
        this.checkProfileImage= checkProfileImage;
        this.name = user.getUserName();
        this.age = getAge(user.getUserBirth());
        this.birth = user.getUserBirth().toString();
        this.message= message;
    }

    private Integer getAge(LocalDate birth){
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);
        Integer birthYear = birth.getYear();
        return (currentYear - birthYear + 1);
    }
}
