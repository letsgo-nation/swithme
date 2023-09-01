package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity

//@Component
// 엔티티를 컴포넘트로 등록하면 안된다.
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;
    private String token;

    public TokenBlacklist() {
    }

    public TokenBlacklist(String token) {
        this.token = token;
    }
}