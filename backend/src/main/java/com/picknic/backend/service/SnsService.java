package com.picknic.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * AWS SNS를 통한 알림 발행 서비스
 * Hot 투표 알림을 AWS Lambda로 전송하여 모든 사용자에게 알림 전달
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {

    @Value("${aws.sns.topic-arn}")
    private String topicArn;

    @Value("${aws.sns.region}")
    private String region;

    @Value("${aws.sns.access-key}")
    private String accessKey;

    @Value("${aws.sns.secret-key}")
    private String secretKey;

    private SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        log.info("SnsClient initialized for topic: {}", topicArn);
    }

    /**
     * Hot 투표 알림을 SNS로 발행
     * Redis-style 에러 처리: 실패 시 로그만 남기고 예외를 던지지 않음 (fault-tolerant)
     *
     * @param voteId 투표 ID
     * @param voteTitle 투표 제목
     * @param category 투표 카테고리
     */
    public void publishHotVoteNotification(Long voteId, String voteTitle, String category) {
        try {
            // JSON 메시지 생성
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "HOT_VOTE");
            messageData.put("voteId", voteId);
            messageData.put("voteTitle", voteTitle);
            messageData.put("category", category);
            messageData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            String messageJson = objectMapper.writeValueAsString(messageData);

            // SNS 메시지 발행
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(messageJson)
                    .subject("Hot Vote Notification")
                    .build();

            PublishResponse response = snsClient.publish(publishRequest);

            log.info("Hot vote notification published successfully. VoteId: {}, MessageId: {}",
                    voteId, response.messageId());

        } catch (Exception e) {
            // Redis-style 에러 처리: 로그만 남기고 예외를 던지지 않음
            // SNS 실패가 트랜잭션 전체를 실패시키지 않도록 함
            log.error("Failed to publish Hot vote notification to SNS. VoteId: {}, Error: {}",
                    voteId, e.getMessage(), e);
        }
    }
}
