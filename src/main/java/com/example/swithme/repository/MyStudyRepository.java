package com.example.swithme.repository;

import com.example.swithme.entity.MyStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyStudyRepository extends JpaRepository<MyStudy, Long> {
    List<MyStudy> findAllByOrderByModifiedAtDesc();
    List<MyStudy> findAllByCategory_IdOrderByModifiedAtDesc(Long category_id);
}