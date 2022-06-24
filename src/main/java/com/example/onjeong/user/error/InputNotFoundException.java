package com.example.onjeong.user.error;

import lombok.extern.log4j.Log4j2;

//닉네임과 비밀번호가 제대로 전달되지 않았을 경우
@Log4j2
public class InputNotFoundException extends RuntimeException{
    public InputNotFoundException(){
        super();
        log.error("error message: InputNotFoundException");
    }
}
