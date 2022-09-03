package com.example.onjeong.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final JobConfig jobConfig;

    // 매주 월요일 0시에 실행
    @Scheduled(cron="0 38 14 ? * WED")
    public void task() {

        try {
            System.out.println("배치 시작");
            Map<String, JobParameter> confMap = new HashMap<>();
            confMap.put("time", new JobParameter(System.currentTimeMillis()));
            jobLauncher.run(jobConfig.simpleJob(), new JobParameters(confMap));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}