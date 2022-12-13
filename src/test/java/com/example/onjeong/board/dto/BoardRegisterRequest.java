package com.example.onjeong.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BoardRegisterRequest {
    private MultipartFile multipartFile;
    private String boardContent;
}
