package com.example.onjeong;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class OnjeongApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties";

	public static void main(String[] args) {
		new SpringApplicationBuilder(OnjeongApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}
}

