package com.picknic.backend.event;

import com.picknic.backend.domain.PointType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 투표 완료 이벤트
 *
 * 투표 참여 또는 투표 생성이 완료되었을 때 발행되는 이벤트
 * 포인트 적립을 위해 PointEventListener에서 수신됨
 */
@Getter
public class VoteCompletedEvent extends ApplicationEvent {

    /**
     * 투표한 사용자 ID
     */
    private final String userId;

    /**
     * 투표 ID (referenceId로 사용)
     */
    private final Long voteId;

    /**
     * 포인트 타입 (VOTE 또는 CREATE)
     */
    private final PointType type;

    /**
     * 포인트 양
     */
    private final int amount;

    /**
     * 학교명 (optional, nullable)
     */
    private final String schoolName;

    /**
     * VoteCompletedEvent 생성자
     *
     * @param source 이벤트를 발행한 객체
     * @param userId 투표한 사용자 ID
     * @param voteId 투표 ID
     * @param type 포인트 타입 (VOTE 또는 CREATE)
     * @param amount 포인트 양
     * @param schoolName 학교명 (nullable)
     */
    public VoteCompletedEvent(Object source, String userId, Long voteId,
                              PointType type, int amount, String schoolName) {
        super(source);
        this.userId = userId;
        this.voteId = voteId;
        this.type = type;
        this.amount = amount;
        this.schoolName = schoolName;
    }
}
