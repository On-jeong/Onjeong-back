package com.example.onjeong.profile.dto;

import com.example.onjeong.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Calendar;

@Getter
@NoArgsConstructor
public class UserInformationDto {
    private String profileImageUrl;
    private boolean checkProfileImage;
    private String name;
    private Integer age;
    private String birth;

    @Builder
    public UserInformationDto(User user,String profileImageUrl,boolean checkProfileImage) {
        this.profileImageUrl= profileImageUrl;
        this.checkProfileImage= checkProfileImage;
        this.name = user.getUserName();
        this.age = getAge(user.getUserBirth());
        this.birth = user.getUserBirth().toString();
    }

    private Integer getAge(LocalDate birth){
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);
        Integer birthYear = birth.getYear();
        return (currentYear - birthYear + 1);
    }
}
