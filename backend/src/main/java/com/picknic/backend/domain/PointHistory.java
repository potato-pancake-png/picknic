package com.picknic.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "point_history")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;

    private long amount; // 변동량 (+ 또는 -)

    private String description;

    private String referenceId; // voteId 또는 rewardId

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public PointHistory(String userId, PointType type, long amount, String description, String referenceId) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.referenceId = referenceId;
    }
}