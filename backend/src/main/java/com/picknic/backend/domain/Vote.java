package com.picknic.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "votes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private String creatorId;

    private String category;

    private String schoolName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VoteOption> options = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalVotes = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isHot = false;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void incrementTotalVotes() {
        this.totalVotes++;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addOption(VoteOption option) {
        this.options.add(option);
        option.setVote(this);
    }

    public void updateInfo(String title, String description, String category, LocalDateTime expiresAt, String imageUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.expiresAt = expiresAt;
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    // close 메서드 추가
    public void close() {
        this.isActive = false;
        this.isHot = false;  // 투표 종료 시 Hot 상태도 자동 해제
    }

    // Hot 투표 관련 메서드
    public void markAsHot() {
        this.isHot = true;
    }

    public void unmarkAsHot() {
        this.isHot = false;
    }
}