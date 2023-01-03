package com.example.onjeong.question.controller;

import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.coin.domain.CoinHistoryType;
import com.example.onjeong.coin.service.CoinService;
import com.example.onjeong.question.domain.Answer;
import com.example.onjeong.question.dto.AnswerModifyRequestDto;
import com.example.onjeong.question.service.QuestionService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags="Question")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;
    private final CoinService coinService;
    private final FCMService fcmService;

    @ApiOperation(value = "이주의 문답 질문 보여주기")
    @GetMapping("/questions")
    public ResponseEntity<ResultResponse> showQuestion() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_QUESTION_SUCCESS, questionService.showQuestion()));
    }

    @ApiOperation(value = "이주의 문답 답변들 보여주기")
    @GetMapping("/answers")
    public ResponseEntity<ResultResponse> showAllAnswer() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ANSWERS_SUCCESS, questionService.showAllAnswer()));
    }

    @ApiOperation(value = "이주의 문답에 답변한 가족 리스트")
    @GetMapping("/answers-family")
    public ResponseEntity<ResultResponse> showAllAnswerFamily() {
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ANSWERED_FAMILY_SUCCESS, questionService.showAllAnswerFamily()));
    }

    @ApiOperation(value = "이주의 문답 답변 작성하기")
    @PostMapping("/answers/register")
    public ResponseEntity<ResultResponse> registerAnswer(String answerContent) throws FirebaseMessagingException {
        Answer answer = questionService.registerAnswer(answerContent);

        if(questionService.answerFamilyCheck()) coinService.coinSave(CoinHistoryType.ANSWER, 210);
        else coinService.coinSave(CoinHistoryType.ANSWER, 10);

        fcmService.sendAnswer(answer);
        fcmService.sendFamilyCheck(answer);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.POST_ANSWER_SUCCESS));
    }

    @ApiOperation(value = "이주의 문답 답변 수정하기")
    @PutMapping("/answers")
    public ResponseEntity<ResultResponse> modifyAnswer(@RequestBody AnswerModifyRequestDto answerModifyRequestDto) {
        questionService.modifyAnswer(answerModifyRequestDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PUT_ANSWER_SUCCESS));
    }

    @ApiOperation(value = "이주의 문답 답변 삭제하기")
    @DeleteMapping("/answers")
    public ResponseEntity<ResultResponse> deleteAnswer(@RequestParam Long answerId) {
        questionService.deleteAnswer(answerId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_ANSWER_SUCCESS));
    }

}
