package com.example.onjeong.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ConfigurationProperties(prefix = "batch")
@Configuration
public class BatchConfig {
    private int chunk = 1;
}
