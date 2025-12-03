package com.picknic.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote_options")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    @Setter
    private Vote vote;

    @Column(nullable = false)
    private String optionText;

    @Column(nullable = false)
    @Builder.Default
    private Integer voteCount = 0;

    private String imageUrl;

    public void incrementVoteCount() {
        this.voteCount++;
    }
}