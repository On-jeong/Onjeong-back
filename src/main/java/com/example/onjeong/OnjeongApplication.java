package com.example.onjeong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@ComponentScan(basePackages = "com.example.onjeong")
@EnableSwagger2
@ComponentScan(basePackages = "com.example.onjeong.controller")
@SpringBootApplication
public class OnjeongApplication{

	public static void main(String[] args) {
		SpringApplication.run(OnjeongApplication.class, args);
	}

}

