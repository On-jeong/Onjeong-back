package com.example.onjeong.question.controller;

import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.question.dto.AnswerDto;
import com.example.onjeong.question.dto.AnswerModifyRequestDto;
import com.example.onjeong.question.dto.AnswerRequestDto;
import com.example.onjeong.question.dto.QuestionDto;
import com.example.onjeong.question.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="Question")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;
    private final CoinService coinService;

    @ApiOperation(value = "이주의 문답 질문 등록하기 - 관리자용")
    @PostMapping("/questions")
    public ResponseEntity<Boolean> showQuestion(String questionContent) {
        return ResponseEntity.ok(questionService.registerQuestion(questionContent));
    }

    @ApiOperation(value = "이주의 문답 질문 보여주기")
    @GetMapping("/questions")
    public ResponseEntity<QuestionDto> showQuestion() {
        return ResponseEntity.ok(questionService.showQuestion());
    }

    @ApiOperation(value = "이주의 문답 답변들 보여주기")
    @GetMapping("/answers")
    public ResponseEntity<List<AnswerDto>> showAllAnswer() {
        return ResponseEntity.ok(questionService.showAllAnswer());
    }

    @ApiOperation(value = "이주의 문답에 답변한 가족 리스트")
    @GetMapping("/answers-family")
    public ResponseEntity<List<String>> showAllAnswerFamily() {
        return ResponseEntity.ok(questionService.showAllAnswerFamily());
    }

    @ApiOperation(value = "이주의 문답 답변 작성하기")
    @PostMapping("/answers/register")
    public ResponseEntity<AnswerDto> registerAnswer(String answerContent) {
        coinService.coinSave(CoinHistoryType.MAIL, 10);
        return ResponseEntity.ok(questionService.registerAnswer(answerContent));
    }

    @ApiOperation(value = "이주의 문답 답변 수정하기")
    @PutMapping("/answers")
    public ResponseEntity<AnswerDto> modifyAnswer(@RequestBody AnswerModifyRequestDto answerModifyRequestDto) {
        return ResponseEntity.ok(questionService.modifyAnswer(answerModifyRequestDto));
    }

    @ApiOperation(value = "이주의 문답 답변 삭제하기")
    @DeleteMapping("/answers")
    public ResponseEntity<Boolean> deleteAnswer(@RequestParam Long answerId) {
        return ResponseEntity.ok(questionService.deleteAnswer(answerId));
    }

}