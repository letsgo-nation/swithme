package com.example.swithme.controller;

import com.example.swithme.dto.RecordDto;
import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.exception.RecordTimeException;
import com.example.swithme.repository.AccumulatedTimeRepository;
import com.example.swithme.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public ResponseEntity<String> recordTime(@RequestBody RecordDto recordDto) {
        try {
            recordService.recordTime(recordDto.getRecordedTime());
            return ResponseEntity.ok("시간이 성공적으로 기록되었습니다.");
        } catch (RecordTimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("시간을 기록하는 중에 오류가 발생했습니다.");
        }
    }
}


