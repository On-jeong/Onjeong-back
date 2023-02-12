package com.example.onjeong;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class BatchApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		//SpringApplication.run(OnjeongApplication.class, args);
		new SpringApplicationBuilder(BatchApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}
}

