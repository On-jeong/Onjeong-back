package com.example.onjeong.writer;

import com.example.onjeong.question.domain.Question;
import com.example.onjeong.question.repository.PureQuestionRepository;
import com.example.onjeong.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WeeklyQuestionItemWriter implements ItemWriter<Question> {

    private final QuestionRepository questionRepository;
    private final PureQuestionRepository pureQuestionRepository;
    private int deleted = 0;

    @Override
    public void write(List<? extends Question> items) throws Exception {
        saveQuestion((List<Question>) items);
    }

    public void saveQuestion(List<Question> items) {
        questionRepository.saveAll(items);
    }

}