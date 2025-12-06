package com.picknic.backend.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Hot 투표 지정 이벤트
 *
 * 관리자가 투표를 Hot으로 지정/해제했을 때 발행되는 이벤트
 * HotVoteEventListener에서 수신하여 SNS 알림 발송
 */
@Getter
public class HotVoteEvent extends ApplicationEvent {

    /**
     * 투표 ID
     */
    private final Long voteId;

    /**
     * 투표 제목
     */
    private final String voteTitle;

    /**
     * 투표 카테고리
     */
    private final String category;

    /**
     * Hot으로 마킹되었는지 여부 (true: Hot 지정, false: Hot 해제)
     */
    private final Boolean isMarkedHot;

    /**
     * HotVoteEvent 생성자
     *
     * @param source 이벤트를 발행한 객체
     * @param voteId 투표 ID
     * @param voteTitle 투표 제목
     * @param category 투표 카테고리
     * @param isMarkedHot Hot으로 마킹되었는지 여부
     */
    public HotVoteEvent(Object source, Long voteId, String voteTitle, String category, Boolean isMarkedHot) {
        super(source);
        this.voteId = voteId;
        this.voteTitle = voteTitle;
        this.category = category;
        this.isMarkedHot = isMarkedHot;
    }
}
