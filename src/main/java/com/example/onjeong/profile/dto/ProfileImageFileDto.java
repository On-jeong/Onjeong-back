package com.example.onjeong.profile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageFileDto {
    private MultipartFile multipartFile;
}
