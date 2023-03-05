package com.example.onjeong.infra;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WebRestController {

    private final Environment env;

    @GetMapping("/current-profile")
    public String getProfile () {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("server", "server1", "server2");

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}