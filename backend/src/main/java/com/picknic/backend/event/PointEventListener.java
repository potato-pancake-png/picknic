package com.picknic.backend.event;

import com.picknic.backend.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 포인트 이벤트 리스너
 *
 * VoteCompletedEvent를 수신하여 포인트 적립을 처리하는 리스너
 * 이벤트 기반 아키텍처를 통해 투표 모듈과 포인트 모듈의 결합도를 낮춤
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PointEventListener {

    private final PointService pointService;

    /**
     * 투표 완료 이벤트 처리
     *
     * VoteCompletedEvent가 발행되면 자동으로 호출되어 포인트를 적립함
     * 예외가 발생해도 다른 리스너나 비즈니스 로직에 영향을 주지 않도록 try-catch로 감쌈
     *
     * @param event VoteCompletedEvent 객체
     */
    @EventListener
    public void handleVoteCompleted(VoteCompletedEvent event) {
        log.info("VoteCompletedEvent 수신 - userId: {}, voteId: {}, type: {}, amount: {}",
                event.getUserId(), event.getVoteId(), event.getType(), event.getAmount());

        try {
            // 포인트 적립 처리 (voteId를 referenceId로 전달)
            pointService.earnPoints(
                    event.getUserId(),
                    event.getType(),
                    event.getAmount(),
                    event.getSchoolName(),
                    String.valueOf(event.getVoteId()) // referenceId
            );

            log.info("포인트 적립 완료 - userId: {}, voteId: {}, type: {}, amount: {}",
                    event.getUserId(), event.getVoteId(), event.getType(), event.getAmount());

        } catch (IllegalStateException e) {
            // 비즈니스 로직 예외 (일일 제한 초과 등)
            log.warn("포인트 적립 실패 (비즈니스 규칙 위반) - userId: {}, error: {}",
                    event.getUserId(), e.getMessage());

        } catch (Exception e) {
            // 시스템 예외 (DB 연결 오류, Redis 오류 등)
            log.error("포인트 적립 실패 (시스템 오류) - userId: {}, voteId: {}, error: {}",
                    event.getUserId(), event.getVoteId(), e.getMessage(), e);
        }
    }
}
