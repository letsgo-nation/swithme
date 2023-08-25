package com.example.swithme.service;

import com.example.swithme.entity.MyStudy;
import com.example.swithme.repository.MyStudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    MyStudyRepository myStudyRepository;


    public List<MyStudy> findAll() {
        List<MyStudy> findAll = myStudyRepository.findAll();
        return findAll;
    }
}

