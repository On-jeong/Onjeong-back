package com.example.onjeong.controller;

import com.example.onjeong.domain.Question;
import com.example.onjeong.dto.*;
import com.example.onjeong.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags="Question")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @ApiOperation(value = "이주의 문답 질문 등록하기 - 관리자용")
    @PostMapping("/questions")
    public ResponseEntity<Boolean> showQuestion(String questionContent) {
        return ResponseEntity.ok(questionService.registerQuestion(questionContent));
    }

    @ApiOperation(value = "이주의 문답 질문 보여주기")
    @GetMapping("/questions")
    public ResponseEntity<QuestionDto> showQuestion(Long familyId) {
        return ResponseEntity.ok(questionService.showQuestion(familyId));
    }

    @ApiOperation(value = "이주의 문답 답변들 보여주기")
    @GetMapping("/mails")
    public ResponseEntity<List<AnswerDto>> showAllAnswer(@RequestParam Long questionId) {
        return ResponseEntity.ok(questionService.showAllAnswer(questionId));
    }

    @ApiOperation(value = "이주의 문답 답변 작성하기")
    @PostMapping("/mails/register")
    public ResponseEntity<AnswerDto> registerAnswer(@RequestBody AnswerRequestDto answerRequestDto) {
        return ResponseEntity.ok(questionService.registerAnswer(answerRequestDto));
    }

    @ApiOperation(value = "이주의 문답 답변 수정하기")
    @PutMapping("/answers")
    public ResponseEntity<AnswerDto> modifyAnswer(@RequestBody AnswerModifyRequestDto answerModifyRequestDto) {
        return ResponseEntity.ok(questionService.modifyAnswer(answerModifyRequestDto));
    }

    @ApiOperation(value = "이주의 문답 답변 삭제하기")
    @DeleteMapping("/mails")
    public ResponseEntity<Boolean> deleteAnswer(@RequestParam Long answerId) {
        return ResponseEntity.ok(questionService.deleteAnswer(answerId));
    }

}
