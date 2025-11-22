package com.picknic.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_points")
public class UserPoint {

    @Id
    @Column(nullable = false, unique = true)
    private String userId; // 로그인 ID (Auth 모듈에서 옴)

    private long currentPoints; // 사용 가능한 포인트

    private long totalAccumulatedPoints; // 랭킹용 누적 포인트

    @Version // 동시성 제어 (낙관적 락)
    private Long version;

    public UserPoint(String userId) {
        this.userId = userId;
        this.currentPoints = 0;
        this.totalAccumulatedPoints = 0;
    }

    // 포인트 적립
    public void addPoints(long amount) {
        this.currentPoints += amount;
        this.totalAccumulatedPoints += amount;
    }

    // 포인트 사용
    public void usePoints(long amount) {
        if (this.currentPoints < amount) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
        this.currentPoints -= amount;
    }
}