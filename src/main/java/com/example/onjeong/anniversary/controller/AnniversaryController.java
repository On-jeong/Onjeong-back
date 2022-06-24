package com.example.onjeong.anniversary.controller;

import com.example.onjeong.anniversary.domain.Anniversary;
import com.example.onjeong.anniversary.dto.AnniversaryModifyDto;
import com.example.onjeong.anniversary.dto.AnniversaryRegisterDto;
import com.example.onjeong.anniversary.service.AnniversaryService;
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
import java.util.Map;

@Api(tags="Anniversary")
@RequiredArgsConstructor
@RestController
@Log4j2
public class AnniversaryController {
    private final AnniversaryService anniversaryService;

    @ApiOperation(value="월별 모든 특수일정 가져오기")
    @GetMapping(value = "/anniversaries/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<LocalDate,Anniversary>>> allAnniversaryGet(@PathVariable("anniversaryDate") String anniversaryDate){
        List<Map<LocalDate,Anniversary>> result= anniversaryService.allAnniversaryGet(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="해당 일의 특수일정 가져오기")
    @GetMapping(value = "/anniversary/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Anniversary>> anniversaryGet(@PathVariable("anniversaryDate") String anniversaryDate){
        List<Anniversary> result= anniversaryService.anniversaryGet(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="해당 일의 특수일정 등록하기")
    @PostMapping(value = "/anniversary/{anniversaryDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Anniversary> anniversaryRegister(@PathVariable("anniversaryDate") String anniversaryDate, @RequestBody AnniversaryRegisterDto anniversaryRegisterDto){
        Anniversary result= anniversaryService.anniversaryRegister(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE),anniversaryRegisterDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="해당 일의 특수일정 수정하기")
    @PutMapping(value = "/anniversary/{anniversaryDate}/{anniversaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Anniversary> anniversaryModify(@PathVariable("anniversaryDate") String anniversaryDate,@PathVariable("anniversaryId") Long anniversaryId, @RequestBody AnniversaryModifyDto anniversaryModifyDto){
        Anniversary result= anniversaryService.anniversaryModify(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE),anniversaryId,anniversaryModifyDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="해당 일의 특수일정 삭제하기")
    @DeleteMapping(value = "/anniversary/{anniversaryDate}/{anniversaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> anniversaryRemove(@PathVariable("anniversaryDate") String anniversaryDate,@PathVariable("anniversaryId") Long anniversaryId){
        String result= anniversaryService.anniversaryRemove(LocalDate.parse(anniversaryDate, DateTimeFormatter.ISO_DATE),anniversaryId);
        return ResponseEntity.ok(result);
    }
}
