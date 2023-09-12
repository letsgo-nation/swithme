package com.example.swithme.dto;

import com.example.swithme.entity.Calendar;

import java.time.LocalDateTime;

public class CalendarResponseDto {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public CalendarResponseDto(Calendar calendar) {
        this.id = calendar.getId();
        this.name = calendar.getName();
        this.startTime = calendar.getStartTime();
        this.endTime = calendar.getEndTime();
    }
}
