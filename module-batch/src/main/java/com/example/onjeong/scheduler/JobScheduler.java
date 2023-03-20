package com.example.onjeong.scheduler;
import com.example.onjeong.job.WeeklyQuestionBuildJob;
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
    private final WeeklyQuestionBuildJob weeklyQuestionBuildJob;

    // 매주 월요일 0시에 실행
    @Scheduled(cron="0 00 00 ? * MON")
    public void runWeeklyQuestionBuildJob() {
        try {
            Map<String, JobParameter> confMap = new HashMap<>();
            confMap.put("requestDate", new JobParameter(System.currentTimeMillis()));
            jobLauncher.run(weeklyQuestionBuildJob.questionBuildJob(), new JobParameters(confMap));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}