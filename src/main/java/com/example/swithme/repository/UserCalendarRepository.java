package com.example.swithme.repository;

import com.example.swithme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCalendarRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
