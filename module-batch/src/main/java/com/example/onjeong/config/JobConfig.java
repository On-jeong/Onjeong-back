package com.example.onjeong.config;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.family.repository.FamilyRepository;
import com.example.onjeong.mail.domain.Mail;
import com.example.onjeong.mail.repository.MailRepository;
import com.example.onjeong.question.domain.PureQuestion;
import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.PureQuestionRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.naming.factory.MailSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PureQuestionRepository pureQuestionRepository;
    private final FamilyRepository familyRepository;
    private final QuestionRepository questionRepository;
    private final MailRepository mailRepository;
    private final UserRepository userRepository;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(questionBuildStep())
                .next(mailDeleteStep())
                .build();
    }

    @Bean
    public Step questionBuildStep() {
        return stepBuilderFactory.get("questionBuildStep")
                .tasklet((contribution, chunkContext) -> {
                    PureQuestion pureQuestion = pureQuestionRepository.chooseWeeklyQuestion();
                    List<Family> families = familyRepository.findAll();
                    List<Question> questionList = new ArrayList<>();

                    for(Family f : families){
                        Question q = Question.builder()
                                .questionContent(pureQuestion.getPureQuestionContent())
                                .questionTime(LocalDateTime.now())
                                .family(f)
                                .build();
                        questionList.add(q);
                    }

                    questionRepository.saveAll(questionList);
                    pureQuestionRepository.delete(pureQuestion);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step mailDeleteStep() {
        return stepBuilderFactory.get("mailDeleteStep")
                .tasklet((contribution, chunkContext) -> {
//                    List<User> users = userRepository.findAll();
//                    for(User u : users){
//                        List<Mail> validMailReceiveList = mailRepository.findByReceiver(u.getUserId());
//                        List<Mail> validMailSendList = mailRepository.findBySender(u.getUserId());
//                        List<Mail> allMailList = mailRepository.findByUser(u.getUserId());
//                        for(Mail m : allMailList){
//                            if(!validMailReceiveList.contains(m) && !validMailSendList.contains(m))
//                                mailRepository.delete(m);
//                        }
//                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}