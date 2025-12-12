package com.picknic.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 비동기 처리 설정
 *
 * @Async 어노테이션이 붙은 메서드를 별도의 스레드 풀에서 비동기로 실행하도록 설정
 * Hot 투표 이벤트 등 시간이 오래 걸리는 작업을 백그라운드에서 처리하여 API 응답 속도 개선
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 비동기 작업을 실행할 스레드 풀 설정
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (항상 유지)
        executor.setCorePoolSize(5);

        // 최대 스레드 수 (부하가 높을 때 증가)
        executor.setMaxPoolSize(10);

        // 대기 큐 크기 (최대 스레드 수 초과 시 대기)
        executor.setQueueCapacity(100);

        // 스레드 이름 접두사 (로그에서 식별 용이)
        executor.setThreadNamePrefix("async-event-");

        executor.initialize();
        return executor;
    }
}
