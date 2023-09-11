package com.example.swithme.repository;

import com.example.swithme.entity.GroupDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDetailsRepository extends JpaRepository<GroupDetails, Long> {
    List<GroupDetails> findAllByGroupId(Long id);
}
