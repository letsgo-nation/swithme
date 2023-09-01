package com.example.swithme.service;

import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.exception.RecordTimeException;
import com.example.swithme.repository.AccumulatedTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {
    private final AccumulatedTimeRepository accumulatedTimeRepository;

    @Autowired
    public RecordServiceImpl(AccumulatedTimeRepository accumulatedTimeRepository) {
        this.accumulatedTimeRepository = accumulatedTimeRepository;
    }

    @Override
    public void recordTime(String recordedTime) {
        try {
            // 기존에 저장된 누적 시간을 조회
            AccumulatedTime accumulatedTime = accumulatedTimeRepository.findById(1L)
                    .orElse(new AccumulatedTime());

            // 누적 시간 업데이트
            long newAccumulatedMinutes = accumulatedTime.getAccumulatedMinutes() +
                    parseRecordedTime(recordedTime);
            accumulatedTime.setAccumulatedMinutes(newAccumulatedMinutes);

            // 업데이트된 누적 시간 저장
            accumulatedTimeRepository.save(accumulatedTime);
        } catch (Exception e) {
            throw new RecordTimeException("시간을 기록하는 중에 오류가 발생했습니다.");
        }
    }

    // 문자열 형식의 시간을 분 단위로 파싱하는 메서드 (이 부분은 필요에 따라 수정)
    private long parseRecordedTime(String recordedTime) {
        // 예: "01:30" 형식의 문자열을 파싱하여 분 단위로 변환
        String[] parts = recordedTime.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }
}

