package com.example.onjeong.board.controller;


import com.example.onjeong.board.domain.Board;
import com.example.onjeong.board.dto.BoardModifyDto;
import com.example.onjeong.board.dto.BoardRegisterDto;
import com.example.onjeong.board.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags="Board")
@RequiredArgsConstructor
@RestController
@Log4j2
public class BoardController {
    private final BoardService boardService;

    @ApiOperation(value="오늘의 기록 모두 가져오기")
    @GetMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Board>> allBoardGet(@PathVariable("boardDate") String boardDate){
        List<Board> result= boardService.allBoardGet(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 작성하기")
    @PostMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Board> boardRegister(@PathVariable("boardDate") String boardDate, @RequestBody BoardRegisterDto boardRegisterDto){
        Board result= boardService.boardRegister(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE),boardRegisterDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 한개 가져오기")
    @GetMapping(value = "/boards/{boardDate}/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Board> boardGet(@PathVariable("boardDate") String boardDate, @PathVariable("boardId") Long boardId){
        Board result= boardService.boardGet(boardId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 수정하기")
    @PatchMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Board> boardModify(@PathVariable("boardId") Long boardId, @RequestBody BoardModifyDto boardModifyDto){
        Board result= boardService.boardModify(boardId,boardModifyDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 삭제하기")
    @DeleteMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> boardRemove(@PathVariable("boardId") Long boardId){
        String result= boardService.boardRemove(boardId);
        return ResponseEntity.ok(result);
    }
}
