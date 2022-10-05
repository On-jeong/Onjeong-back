package com.example.onjeong.anniversary.controller;

import com.example.onjeong.anniversary.dto.AnniversaryDto;
import com.example.onjeong.anniversary.dto.AnniversaryModifyDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.service.AnniversaryService;
import com.example.onjeong.result.ResultCode;
import com.example.onjeong.result.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Api(tags="Anniversary")
@RequiredArgsConstructor
@RestController
@Log4j2
public class AnniversaryController {
    private final AnniversaryService anniversaryService;

    @ApiOperation(value="월별 모든 특수일정 가져오기")
    @GetMapping(value = "/anniversaries/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> allAnniversaryGet(@PathVariable("anniversaryDate") String anniversaryDate){
        List<AnniversaryDto> data= anniversaryService.allAnniversaryGet(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ALL_ANNIVERSARY_SUCCESS,data));
    }

    @ApiOperation(value="해당 일의 특수일정 가져오기")
    @GetMapping(value = "/anniversaries/days/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> anniversaryGet(@PathVariable("anniversaryDate") String anniversaryDate){
        List<AnniversaryDto> data= anniversaryService.anniversaryGet(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_ANNIVERSARY_SUCCESS,data));
    }

    @ApiOperation(value="해당 일의 특수일정 등록하기")
    @PostMapping(value = "/anniversaries/days/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> anniversaryRegister(@PathVariable("anniversaryDate") String anniversaryDate, @RequestBody AnniversaryRegisterDto anniversaryRegisterDto){
        anniversaryService.anniversaryRegister(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE),anniversaryRegisterDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_ANNIVERSARY_SUCCESS));
    }

    @ApiOperation(value="해당 일의 특수일정 수정하기")
    @PutMapping(value = "/anniversaries/days/{anniversaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> anniversaryModify(@PathVariable("anniversaryId") Long anniversaryId, @RequestBody AnniversaryModifyDto anniversaryModifyDto){
        anniversaryService.anniversaryModify(anniversaryId,anniversaryModifyDto);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_ANNIVERSARY_SUCCESS));
    }

    @ApiOperation(value="해당 일의 특수일정 삭제하기")
    @DeleteMapping(value = "/anniversaries/days/{anniversaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> anniversaryRemove(@PathVariable("anniversaryId") Long anniversaryId){
        anniversaryService.anniversaryRemove(anniversaryId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_ANNIVERSARY_SUCCESS));
    }
}
