package com.picknic.backend.event;

import com.picknic.backend.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Hot 투표 이벤트 리스너
 *
 * HotVoteEvent를 수신하여 AWS SNS 알림을 발송하는 리스너
 * 이벤트 기반 아키텍처를 통해 투표 모듈과 알림 모듈의 결합도를 낮춤
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HotVoteEventListener {

    private final SnsService snsService;

    /**
     * Hot 투표 이벤트 처리
     *
     * HotVoteEvent가 발행되면 자동으로 호출되어 SNS로 알림을 발송함
     * 예외가 발생해도 다른 리스너나 비즈니스 로직에 영향을 주지 않도록 try-catch로 감쌈
     *
     * @param event HotVoteEvent 객체
     */
    @EventListener
    public void handleHotVoteMarked(HotVoteEvent event) {
        log.info("HotVoteEvent 수신 - voteId: {}, title: {}, isMarkedHot: {}",
                event.getVoteId(), event.getVoteTitle(), event.getIsMarkedHot());

        try {
            // Hot으로 마킹된 경우에만 SNS 알림 발송 (해제 시에는 발송하지 않음)
            if (event.getIsMarkedHot()) {
                snsService.publishHotVoteNotification(
                        event.getVoteId(),
                        event.getVoteTitle(),
                        event.getCategory()
                );

                log.info("Hot 투표 SNS 알림 발송 완료 - voteId: {}, title: {}",
                        event.getVoteId(), event.getVoteTitle());
            } else {
                log.info("Hot 투표 해제 - SNS 알림 발송하지 않음 - voteId: {}",
                        event.getVoteId());
            }

        } catch (Exception e) {
            // 시스템 예외 (SNS 연결 오류 등)
            // SnsService 내부에서 이미 예외를 처리하지만, 추가 안전장치
            log.error("Hot 투표 이벤트 처리 실패 - voteId: {}, error: {}",
                    event.getVoteId(), e.getMessage(), e);
        }
    }
}
