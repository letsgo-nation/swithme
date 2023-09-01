package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="accumulatedtime")
public class AccumulatedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accumulated_time")
    private Long accumulatedMinutes;

    // User : AccumulatedTime 간에 1:1 관계 설정 방법 1
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public AccumulatedTime(Long accumulatedMinutes) {
        this.accumulatedMinutes = accumulatedMinutes;

    }

    public AccumulatedTime() {
        this.accumulatedMinutes = accumulatedMinutes;
    }



}
