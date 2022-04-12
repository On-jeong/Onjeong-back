package com.example.onjeong.controller;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super(email + " NotFoundException");
    }
}
