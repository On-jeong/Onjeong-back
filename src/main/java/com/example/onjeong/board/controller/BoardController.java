package com.example.onjeong.board.controller;


import com.example.onjeong.board.dto.BoardDto;
import com.example.onjeong.board.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags="Board")
@RequiredArgsConstructor
@RestController
@Log4j2
public class BoardController {
    private final BoardService boardService;
    private HttpServletRequest httpServletRequest;

    @ApiOperation(value="오늘의 기록 모두 가져오기")
    @GetMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardDto>> allBoardGet(@PathVariable("boardDate") String boardDate){
        List<BoardDto> result= boardService.allBoardGet(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 작성하기")
    @PostMapping(value = "/boards/{boardDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> boardRegister(@PathVariable("boardDate") String boardDate, @RequestPart(value = "images", required = false) MultipartFile multipartFile, @RequestPart(value = "boardContent") String boardContent){
        String result= boardService.boardRegister(LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE), multipartFile, boardContent);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 한개 가져오기")
    @GetMapping(value = "/boards/{boardId}/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> boardGet(@PathVariable("boardId") Long boardId){
        BoardDto result= boardService.boardGet(boardId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 수정하기")
    @PatchMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> boardModify(@PathVariable("boardId") Long boardId, @RequestPart(value = "images", required = false) MultipartFile multipartFile, @RequestParam("boardContent") String boardContent, @RequestParam("boardDate") String boardDate){
        String result= boardService.boardModify(boardId, multipartFile, boardContent, LocalDate.parse(boardDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="오늘의 기록 삭제하기")
    @DeleteMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> boardRemove(@PathVariable("boardId") Long boardId){
        String result= boardService.boardRemove(boardId);
        return ResponseEntity.ok(result);
    }
}
