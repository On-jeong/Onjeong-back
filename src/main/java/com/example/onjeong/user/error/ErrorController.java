package com.example.onjeong.user.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value="/error")
public class ErrorController {

    @GetMapping(value = "/unauthorized")
    public ResponseEntity<Void> unauthorized() {
        log.error("error message: unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}