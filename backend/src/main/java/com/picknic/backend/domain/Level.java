package com.picknic.backend.domain;

import lombok.Getter;

/**
 * 사용자 레벨 시스템
 *
 * API Spec: 비즈니스 로직 명세
 *
 * 포인트 누적량에 따라 레벨이 자동으로 결정됩니다.
 */
@Getter
public enum Level {

    BRONZE("브론즈", "🥉", 0, 499),
    SILVER("실버", "🥈", 500, 1499),
    GOLD("골드", "🥇", 1500, 2999),
    DIAMOND("다이아", "💎", 3000, 4999),
    MASTER("마스터", "🏆", 5000, Integer.MAX_VALUE);

    /**
     * 레벨명 (한글)
     */
    private final String displayName;

    /**
     * 레벨 아이콘 (이모지)
     */
    private final String icon;

    /**
     * 최소 포인트 (inclusive)
     */
    private final int minPoints;

    /**
     * 최대 포인트 (inclusive)
     */
    private final int maxPoints;

    Level(String displayName, String icon, int minPoints, int maxPoints) {
        this.displayName = displayName;
        this.icon = icon;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    /**
     * 포인트로부터 레벨 계산
     *
     * @param points 사용자 누적 포인트
     * @return 해당 포인트에 맞는 Level
     */
    public static Level fromPoints(long points) {
        for (Level level : Level.values()) {
            if (points >= level.minPoints && points <= level.maxPoints) {
                return level;
            }
        }
        // 기본값 (이론적으로 도달 불가능)
        return BRONZE;
    }
}
