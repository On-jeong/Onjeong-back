package com.example.onjeong.board.controller;


import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.board.service.BoardService;
import com.example.onjeong.fcm.FCMService;
import com.example.onjeong.home.domain.CoinHistoryType;
import com.example.onjeong.home.service.CoinService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags="Board")
@RequiredArgsConstructor
@RestController
@Log4j2
public class BoardController {
    private final BoardService boardService;
    private final CoinService coinService;
    private final FCMService fcmService;

    @ApiOperation(value="오늘의 기록 모두 가져오기")
    @GetMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getAllBoard(@PathVariable("boardDate") String boardDate){
        List<BoardDto> data= boardService.getAllBoard(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ALL_BOARD_SUCCESS,data));
    }

    @ApiOperation(value="오늘의 기록 작성하기")
    @PostMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResultResponse> registerBoard(@PathVariable("boardDate") String boardDate, @RequestPart(name = "images", required = false) MultipartFile multipartFile, @RequestPart("boardContent") String boardContent)throws FirebaseMessagingException {
        Board board= boardService.registerBoard(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE), multipartFile, boardContent);
        //fcmService.sendBoard(board);
        coinService.coinSave(CoinHistoryType.BOARD, 20);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_BOARD_SUCCESS));
    }

    @ApiOperation(value="오늘의 기록 한개 가져오기")
    @GetMapping(value = "/boards/{boardId}/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> getOneBoard(@PathVariable("boardId") Long boardId){
        BoardDto data= boardService.getOneBoard(boardId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_BOARD_SUCCESS,data));
    }

    @ApiOperation(value="오늘의 기록 수정하기")
    @PostMapping(value = "/boards/idx/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modifyBoard(@PathVariable("boardId") Long boardId, @RequestPart(name = "images", required = false) MultipartFile multipartFile, @RequestPart("boardContent") String boardContent){
        boardService.modifyBoard(boardId, multipartFile, boardContent);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_BOARD_SUCCESS));
    }

    @ApiOperation(value="오늘의 기록 삭제하기")
    @DeleteMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_BOARD_SUCCESS));
    }
}
