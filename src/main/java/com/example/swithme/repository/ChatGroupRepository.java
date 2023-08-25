package com.example.swithme.repository;

import com.example.swithme.entity.ChatGroup;
import com.example.swithme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    List<ChatGroup> findAllByUser(User user);
}
