package com.example.onjeong.user.error;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userNickname){
        super(userNickname + " NotFoundException");
    }
}
