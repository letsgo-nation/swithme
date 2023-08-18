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
    private Long accumulatedTime;

    // @JsonIgnore
    @OneToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;


}
