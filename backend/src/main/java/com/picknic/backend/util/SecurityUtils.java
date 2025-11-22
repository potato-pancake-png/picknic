package com.picknic.backend.util;

import org.springframework.stereotype.Component;

/**
 * Mock 인증 유틸리티
 *
 * ⚠️ **경고: 이 클래스는 개발/테스트 전용입니다!**
 *
 * 프로덕션 환경에서는 절대 사용하지 마세요.
 * 모든 사용자가 동일한 ID("test_user_123")를 가지게 됩니다.
 *
 * **TODO: Auth 모듈 준비 완료 후 교체 필수**
 * - Spring Security Context 기반 구현으로 교체
 * - JWT 토큰 검증 로직 추가
 * - 실제 사용자 인증/인가 처리
 *
 * 실제 인증 모듈이 준비되면 이 클래스를 교체해야 합니다.
 * Spring Security Context를 사용하지 않습니다.
 */
@Component
public class SecurityUtils {

    /**
     * 현재 로그인된 사용자 ID를 반환합니다 (Mock)
     *
     * ⚠️ 항상 "test_user_123"을 반환합니다 (개발 전용)
     *
     * @return 하드코딩된 테스트 사용자 ID
     */
    public String getCurrentUserId() {
        return "test_user_123";
    }
}
