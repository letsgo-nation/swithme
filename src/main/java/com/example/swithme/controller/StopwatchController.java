package com.example.swithme.controller;

import com.example.swithme.service.StopwatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class StopwatchController {

    @Autowired
    private StopwatchService stopwatchService;

    @GetMapping("/start")
    public void startStopwatch() {
        stopwatchService.start();
    }

    @GetMapping("/stop")
    public void stopStopwatch() {
        stopwatchService.stop();
    }

    @GetMapping("/accumulatedTime")
    public long getAccumulatedTime() {
        return stopwatchService.getAccumulatedTime();
    }

    @GetMapping("/accumulatedMinutes")
    public long getAccumulatedMinutes() {
        return stopwatchService.getAccumulatedMinutes();
    }

    @PostMapping("/saveAccumulatedTime")
    public void saveAccumulatedTime(@RequestBody long accumulatedMinutes) {
        stopwatchService.saveAccumulatedTime(accumulatedMinutes);
    }
}
