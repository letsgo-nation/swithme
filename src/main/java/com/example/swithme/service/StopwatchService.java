package com.example.swithme.service;

import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.repository.AccumulatedTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopwatchService {

    private final AccumulatedTimeRepository accumulatedTimeRepository;

//    public StopwatchService(AccumulatedTimeRepository accumulatedTimeRepository) {
//        this.accumulatedTimeRepository = accumulatedTimeRepository;
//    }


    private long startTime;
    private long accumulatedTime; // 누적 시간
    private long accumulatedMinutes; // 누적 시간을 분으로 만듦

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        if (startTime != 0) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            accumulatedTime += elapsedTime;
            accumulatedMinutes = accumulatedTime / 60000; // 1분 = 60,000 밀리초
            startTime = 0;
        }
    }

    // 누적시간 반환
    public long getAccumulatedTime() {
        return accumulatedTime;
    }

    // 누적시간 조회
    public long getAccumulatedMinutes() {
        return accumulatedMinutes;
    }

    // 누적된 시간을 repo 에 저장하는 메서드

    public void saveAccumulatedTime(long accumulatedMinutes) {
        AccumulatedTime entity = new AccumulatedTime();
        entity.setAccumulatedMinutes(accumulatedMinutes);
        accumulatedTimeRepository.save(entity);
    }
}
