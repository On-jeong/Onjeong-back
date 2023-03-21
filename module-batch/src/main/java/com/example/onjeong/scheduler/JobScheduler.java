package com.example.onjeong.scheduler;
import com.example.onjeong.job.AnniversaryNotificationJob;
import com.example.onjeong.job.NotificationDeleteJob;
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
    private final AnniversaryNotificationJob anniversaryNotificationJob;
    private final NotificationDeleteJob notificationDeleteJob;

    // 매주 월요일 0시에 실행
    @Scheduled(cron="0 0 0 ? * MON")
    public void runWeeklyQuestionBuildJob() {
        try {
            Map<String, JobParameter> confMap = new HashMap<>();
            confMap.put("requestDate", new JobParameter(System.currentTimeMillis()));
            jobLauncher.run(weeklyQuestionBuildJob.questionBuildJob(), new JobParameters(confMap));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 매일 12시에 실행
    @Scheduled(cron="0 0 0 1/1 * ?")
    public void runAnniversaryNotificationBuildJob() {
        try {
            Map<String, JobParameter> confMap = new HashMap<>();
            confMap.put("requestDate", new JobParameter(System.currentTimeMillis()));
            jobLauncher.run(anniversaryNotificationJob.notificationBuildJob(), new JobParameters(confMap));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 매일 새벽 4시에 실행
    @Scheduled(cron="0 0 4 1/1 * ?")
    public void runNotificationDeleteJob() {
        try {
            Map<String, JobParameter> confMap = new HashMap<>();
            confMap.put("requestDate", new JobParameter(System.currentTimeMillis()));
            jobLauncher.run(notificationDeleteJob.notificationsDeleteJob(), new JobParameters(confMap));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}