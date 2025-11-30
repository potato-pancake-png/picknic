# Picknic API 명세서

## 개요
청소년 타겟 투표 기반 커뮤니티 앱 "Picknic"의 백엔드 API 명세서

**Base URL**: `https://api.picknic.app/v1`

**인증 방식**: Bearer Token (JWT)

---

## 1. 인증 (Authentication)

### 1.1 이메일 회원가입
```
POST /auth/signup/email
```

**Request Body**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "username": "지민",
  "birthYear": 2006
}
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "회원가입이 완료되었습니다",
  "data": {
    "userId": "user_123abc",
    "requiresSchoolVerification": true
  }
}
```

---

### 1.2 소셜 로그인 (카카오/구글/애플)
```
POST /auth/login/social
```

**Request Body**
```json
{
  "provider": "kakao|google|apple",
  "accessToken": "social_provider_access_token",
  "username": "지민"
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "userId": "user_123abc",
    "username": "지민",
    "accessToken": "jwt_access_token",
    "refreshToken": "jwt_refresh_token",
    "verifiedSchool": null,
    "points": 0,
    "rank": 9999
  }
}
```

---

### 1.3 학교 인증
```
POST /auth/verify-school
```

**Request Body**
```json
{
  "schoolName": "서울고등학교",
  "verificationCode": "ABC123",
  "verificationMethod": "email|sms|student-id"
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "서울고등학교 인증이 완료되었습니다",
  "data": {
    "schoolName": "서울고등학교",
    "bonusPoints": 50
  }
}
```

---

### 1.4 학교 인증 스킵
```
POST /auth/skip-school-verification
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "학교 인증을 건너뛰었습니다"
}
```

---

### 1.5 로그아웃
```
POST /auth/logout
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "로그아웃 되었습니다"
}
```

---

## 2. 사용자 프로필 (User Profile)

### 2.1 내 프로필 조회
```
GET /users/me
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "userId": "user_123abc",
    "username": "지민",
    "avatar": "지",
    "points": 1750,
    "rank": 6,
    "level": "실버",
    "levelIcon": "🥈",
    "verifiedSchool": "서울고등학교",
    "stats": {
      "votesParticipated": 142,
      "votesCreated": 28,
      "attendanceDays": 23,
      "accuracy": 68
    },
    "nextLevelPoints": 230,
    "levelProgress": 75
  }
}
```

---

### 2.2 프로필 수정
```
PATCH /users/me
```

**Request Body**
```json
{
  "username": "지민",
  "avatar": "지"
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "프로필이 수정되었습니다"
}
```

---

### 2.3 최근 활동 조회
```
GET /users/me/activities
```

**Query Parameters**
- `limit` (optional): 조회할 활동 개수 (기본값: 10)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "type": "vote",
      "text": "평생 떡볶이만 vs 햄버거만",
      "points": 1,
      "timestamp": "2025-11-19T10:30:00Z",
      "timeAgo": "5분 전"
    },
    {
      "type": "create",
      "text": "투표 생성: 오늘 뭐 먹지?",
      "points": 2,
      "timestamp": "2025-11-19T09:30:00Z",
      "timeAgo": "1시간 전"
    },
    {
      "type": "win",
      "text": "정답 맞춤! (발로란트 우승팀)",
      "points": 10,
      "timestamp": "2025-11-19T07:30:00Z",
      "timeAgo": "3시간 전"
    },
    {
      "type": "daily",
      "text": "출석 체크",
      "points": 5,
      "timestamp": "2025-11-19T00:00:00Z",
      "timeAgo": "오늘"
    }
  ]
}
```

---

### 2.4 업적 조회
```
GET /users/me/achievements
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "achievement_1",
      "emoji": "🔥",
      "name": "7일 연속 출석",
      "unlocked": true,
      "unlockedAt": "2025-11-15T00:00:00Z"
    },
    {
      "id": "achievement_2",
      "emoji": "💯",
      "name": "투표 100회 참여",
      "unlocked": true,
      "unlockedAt": "2025-11-10T14:30:00Z"
    },
    {
      "id": "achievement_3",
      "emoji": "🎯",
      "name": "정답률 70%",
      "unlocked": false
    },
    {
      "id": "achievement_4",
      "emoji": "👑",
      "name": "투표왕",
      "unlocked": false
    }
  ]
}
```

---

## 3. 투표 (Votes)

### 3.1 투표 목록 조회 (HOT 피드)
```
GET /votes/hot
```

**Query Parameters**
- `limit` (optional): 조회할 투표 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "vote_1",
      "type": "balance",
      "title": "파인애플 피자 호 VS 불호",
      "description": "영원한 논쟁! 당신의 선택은?",
      "category": "음식",
      "options": [
        {
          "id": "option_1a",
          "text": "호 (맛있다)",
          "emoji": "🍍",
          "votes": 1750
        },
        {
          "id": "option_1b",
          "text": "불호 (말도 안돼)",
          "emoji": "🚫",
          "votes": 5250
        }
      ],
      "totalVotes": 7000,
      "isHot": true,
      "timeLeft": "5시간",
      "endsAt": "2025-11-19T20:00:00Z",
      "points": 1,
      "userVoted": null,
      "schoolName": null,
      "createdAt": "2025-11-19T10:00:00Z"
    }
  ]
}
```

---

### 3.2 투표 목록 조회 (전체 피드)
```
GET /votes
```

**Query Parameters**
- `limit` (optional): 조회할 투표 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)
- `type` (optional): 투표 타입 필터 (`balance|multiple|ox|all`, 기본값: all)
- `category` (optional): 카테고리 필터 (예: `음식`, `패션`, `게임` 등)
- `schoolOnly` (optional): 우리학교만 보기 (`true|false`, 기본값: false)
- `myVotesOnly` (optional): 내가 만든 투표만 보기 (`true|false`, 기본값: false)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "vote_uniform",
      "type": "balance",
      "title": "겨울 교복보다 하복이 더 예쁘다",
      "description": "우리 학교 교복 중 더 예쁜 건?",
      "category": "학교",
      "options": [
        {
          "id": "option_uv_a",
          "text": "맞아, 하복이 더 예뻐",
          "emoji": "👔",
          "votes": 612
        },
        {
          "id": "option_uv_b",
          "text": "무슨 소리 겨울 교복이 더 예쁘다",
          "emoji": "🧥",
          "votes": 288
        }
      ],
      "totalVotes": 900,
      "schoolName": "서울고등학교",
      "timeLeft": "6시간",
      "endsAt": "2025-11-19T22:00:00Z",
      "points": 1,
      "userVoted": null,
      "createdAt": "2025-11-19T08:00:00Z"
    }
  ],
  "pagination": {
    "total": 150,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 3.3 투표 상세 조회
```
GET /votes/{voteId}
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "id": "vote_1",
    "type": "balance",
    "title": "파인애플 피자 호 VS 불호",
    "description": "영원한 논쟁! 당신의 선택은?",
    "category": "음식",
    "options": [
      {
        "id": "option_1a",
        "text": "호 (맛있다)",
        "emoji": "🍍",
        "votes": 1750,
        "percentage": 25
      },
      {
        "id": "option_1b",
        "text": "불호 (말도 안돼)",
        "emoji": "🚫",
        "votes": 5250,
        "percentage": 75
      }
    ],
    "totalVotes": 7000,
    "isHot": true,
    "timeLeft": "5시간",
    "endsAt": "2025-11-19T20:00:00Z",
    "points": 1,
    "userVoted": "option_1b",
    "schoolName": null,
    "createdBy": "user_456def",
    "createdAt": "2025-11-19T10:00:00Z"
  }
}
```

---

### 3.4 투표 생성
```
POST /votes
```

**Request Body**
```json
{
  "type": "balance|multiple|ox",
  "title": "평생 떡볶이만 먹기 vs 평생 햄버거만 먹기",
  "description": "당신의 선택은?",
  "category": "음식",
  "options": [
    {
      "text": "떡볶이",
      "emoji": "🌶️",
      "image": "data:image/png;base64,..."
    },
    {
      "text": "햄버거",
      "emoji": "🍔",
      "image": null
    }
  ],
  "duration": "24h",
  "points": 1,
  "schoolOnly": false
}
```

**Response (201 Created)**
```json
{
  "success": true,
  "message": "투표가 생성되었습니다",
  "data": {
    "voteId": "vote_new123",
    "bonusPoints": 2,
    "newTotalPoints": 1752
  }
}
```

---

### 3.5 투표 참여
```
POST /votes/{voteId}/vote
```

**Request Body**
```json
{
  "optionId": "option_1a"
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "투표 완료! +1 포인트",
  "data": {
    "voteId": "vote_1",
    "selectedOptionId": "option_1a",
    "points": 1,
    "newTotalPoints": 1751,
    "results": [
      {
        "optionId": "option_1a",
        "votes": 1751,
        "percentage": 25
      },
      {
        "optionId": "option_1b",
        "votes": 5250,
        "percentage": 75
      }
    ]
  }
}
```

---

### 3.6 투표 통계 조회
```
GET /votes/{voteId}/stats
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "voteId": "vote_1",
    "title": "파인애플 피자 호 VS 불호",
    "category": "음식",
    "totalVotes": 7000,
    "results": [
      {
        "optionId": "option_1a",
        "text": "호 (맛있다)",
        "emoji": "🍍",
        "votes": 1750,
        "percentage": 25,
        "isWinner": false
      },
      {
        "optionId": "option_1b",
        "text": "불호 (말도 안돼)",
        "emoji": "🚫",
        "votes": 5250,
        "percentage": 75,
        "isWinner": true
      }
    ],
    "demographics": {
      "topParticipant": {
        "age": 19,
        "gender": "여성",
        "percentage": 35
      },
      "breakdown": [
        { "age": 16, "gender": "여성", "percentage": 18 },
        { "age": 17, "gender": "남성", "percentage": 25 },
        { "age": 18, "gender": "여성", "percentage": 22 },
        { "age": 19, "gender": "여성", "percentage": 35 }
      ]
    },
    "geographic": {
      "schoolName": null,
      "schoolParticipationRate": null
    },
    "relatedCategories": ["아이돌", "패션", "음식", "게임"],
    "insights": {
      "hourlyAverage": 2333,
      "isHot": true,
      "trending": true
    }
  }
}
```

---

### 3.7 내가 참여한 투표 조회
```
GET /votes/my-votes
```

**Query Parameters**
- `limit` (optional): 조회할 투표 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "vote_1",
      "type": "balance",
      "title": "파인애플 피자 호 VS 불호",
      "category": "음식",
      "userVoted": "option_1b",
      "votedAt": "2025-11-19T10:30:00Z",
      "isEnded": false,
      "currentResults": {
        "myChoice": {
          "optionId": "option_1b",
          "text": "불호 (말도 안돼)",
          "percentage": 75,
          "isWinning": true
        }
      }
    }
  ],
  "pagination": {
    "total": 142,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 3.8 내가 만든 투표 조회
```
GET /votes/created-by-me
```

**Query Parameters**
- `limit` (optional): 조회할 투표 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "vote_created1",
      "type": "multiple",
      "title": "점심 메뉴 투표",
      "category": "음식",
      "totalVotes": 900,
      "createdAt": "2025-11-18T12:00:00Z",
      "isActive": true,
      "endsAt": "2025-11-20T12:00:00Z"
    }
  ],
  "pagination": {
    "total": 28,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

## 4. 랭킹 (Rankings)

### 4.1 개인 랭킹 조회
```
GET /rankings/personal
```

**Query Parameters**
- `limit` (optional): 조회할 사용자 수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "myRank": {
      "rank": 6,
      "username": "지민",
      "points": 1750,
      "level": "실버",
      "levelIcon": "🥈"
    },
    "rankings": [
      {
        "rank": 1,
        "username": "투표왕",
        "avatar": "투",
        "points": 2850,
        "level": "마스터",
        "levelIcon": "🏆"
      },
      {
        "rank": 2,
        "username": "밸런스의달인",
        "avatar": "밸",
        "points": 2620,
        "level": "다이아",
        "levelIcon": "💎"
      },
      {
        "rank": 3,
        "username": "민초단",
        "avatar": "민",
        "points": 2340,
        "level": "다이아",
        "levelIcon": "💎"
      },
      {
        "rank": 4,
        "username": "깻잎논쟁러",
        "avatar": "깻",
        "points": 2180,
        "level": "골드",
        "levelIcon": "🥇"
      },
      {
        "rank": 5,
        "username": "투표중독",
        "avatar": "투",
        "points": 1950,
        "level": "골드",
        "levelIcon": "🥇"
      },
      {
        "rank": 6,
        "username": "지민",
        "avatar": "지",
        "points": 1750,
        "level": "실버",
        "levelIcon": "🥈",
        "isMe": true
      }
    ]
  },
  "pagination": {
    "total": 5000,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 4.2 학교 랭킹 조회
```
GET /rankings/schools
```

**Query Parameters**
- `limit` (optional): 조회할 학교 수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "mySchool": {
      "rank": 1,
      "schoolName": "서울고등학교",
      "points": 45280,
      "members": 342
    },
    "rankings": [
      {
        "rank": 1,
        "schoolName": "서울고등학교",
        "avatar": "서",
        "points": 45280,
        "members": 342,
        "isMySchool": true
      },
      {
        "rank": 2,
        "schoolName": "강남고등학교",
        "avatar": "강",
        "points": 42150,
        "members": 318
      },
      {
        "rank": 3,
        "schoolName": "부산여고",
        "avatar": "부",
        "points": 38920,
        "members": 295
      }
    ]
  },
  "pagination": {
    "total": 500,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 4.3 주간 랭킹 보상 정보
```
GET /rankings/rewards
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "currentWeek": {
      "startDate": "2025-11-18T00:00:00Z",
      "endDate": "2025-11-24T23:59:59Z"
    },
    "rewards": [
      {
        "rankRange": "1",
        "reward": "스타벅스 기프티콘 3만원",
        "icon": "🥇"
      },
      {
        "rankRange": "2-5",
        "reward": "편의점 기프티콘 1만원",
        "icon": "🥈"
      },
      {
        "rankRange": "6-20",
        "reward": "랜덤박스 참여권",
        "icon": "🥉"
      }
    ]
  }
}
```

---

## 5. 포인트 & 보상 (Points & Rewards)

### 5.1 포인트 내역 조회
```
GET /points/history
```

**Query Parameters**
- `limit` (optional): 조회할 내역 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "currentPoints": 1750,
    "history": [
      {
        "id": "point_1",
        "type": "vote",
        "description": "투표 참여: 파인애플 피자",
        "points": 1,
        "timestamp": "2025-11-19T10:30:00Z"
      },
      {
        "id": "point_2",
        "type": "create",
        "description": "투표 생성",
        "points": 2,
        "timestamp": "2025-11-19T09:00:00Z"
      },
      {
        "id": "point_3",
        "type": "daily",
        "description": "출석 체크",
        "points": 5,
        "timestamp": "2025-11-19T00:00:00Z"
      },
      {
        "id": "point_4",
        "type": "bonus",
        "description": "학교 인증 보너스",
        "points": 50,
        "timestamp": "2025-11-18T15:30:00Z"
      }
    ]
  },
  "pagination": {
    "total": 150,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 5.2 보상 목록 조회
```
GET /rewards
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "reward_1",
      "type": "giftcard",
      "name": "스타벅스 아메리카노",
      "description": "시원한 아메리카노 한 잔",
      "pointCost": 500,
      "stock": 150,
      "image": "https://example.com/rewards/starbucks.png"
    },
    {
      "id": "reward_2",
      "type": "giftcard",
      "name": "편의점 1천원권",
      "description": "GS25/CU/세븐일레븐",
      "pointCost": 100,
      "stock": 500,
      "image": "https://example.com/rewards/convenience.png"
    },
    {
      "id": "reward_3",
      "type": "special",
      "name": "랜덤박스",
      "description": "행운의 상자를 열어보세요!",
      "pointCost": 200,
      "stock": 300,
      "image": "https://example.com/rewards/randombox.png"
    }
  ]
}
```

---

### 5.3 보상 교환
```
POST /rewards/{rewardId}/redeem
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "보상이 교환되었습니다",
  "data": {
    "rewardId": "reward_1",
    "rewardName": "스타벅스 아메리카노",
    "code": "ABCD-1234-EFGH-5678",
    "pointsSpent": 500,
    "remainingPoints": 1250,
    "expiresAt": "2025-12-19T23:59:59Z"
  }
}
```

---

### 5.4 출석 체크
```
POST /daily-check-in
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "출석 완료! +5 포인트",
  "data": {
    "points": 5,
    "consecutiveDays": 7,
    "totalCheckIns": 23,
    "bonusPoints": 0,
    "newTotalPoints": 1755
  }
}
```

---

## 6. 알림 (Notifications)

### 6.1 알림 목록 조회
```
GET /notifications
```

**Query Parameters**
- `limit` (optional): 조회할 알림 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)
- `unreadOnly` (optional): 읽지 않은 알림만 조회 (`true|false`, 기본값: false)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "notif_1",
      "type": "vote_trending",
      "title": "🔥 학교 투표 참여 현황",
      "message": "지금 우리 학교 학생 68%는 '겨울 교복보다 하복이 더 예쁘다'에 투표했어요!",
      "voteId": "vote_uniform",
      "isRead": false,
      "createdAt": "2025-11-19T10:25:00Z",
      "timeAgo": "5분 전"
    },
    {
      "id": "notif_2",
      "type": "vote_ended",
      "title": "투표 종료",
      "message": "참여하신 '이번 월즈 우승팀은?' 투표가 종료되었습니다",
      "voteId": "vote_worlds",
      "isRead": true,
      "createdAt": "2025-11-18T20:00:00Z",
      "timeAgo": "14시간 전"
    },
    {
      "id": "notif_3",
      "type": "reward",
      "title": "🎁 보상 지급",
      "message": "주간 랭킹 6위 달성! 랜덤박스 참여권이 지급되었습니다",
      "isRead": false,
      "createdAt": "2025-11-18T00:00:00Z",
      "timeAgo": "1일 전"
    }
  ],
  "unreadCount": 2,
  "pagination": {
    "total": 45,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### 6.2 알림 읽음 처리
```
PATCH /notifications/{notificationId}/read
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "알림이 읽음 처리되었습니다"
}
```

---

### 6.3 모든 알림 읽음 처리
```
POST /notifications/read-all
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "모든 알림이 읽음 처리되었습니다",
  "data": {
    "markedAsRead": 12
  }
}
```

---

### 6.4 푸시 알림 설정
```
PATCH /notifications/settings
```

**Request Body**
```json
{
  "pushEnabled": true,
  "voteTrending": true,
  "voteEnded": true,
  "newFollower": false,
  "rankingUpdate": true,
  "rewardAvailable": true
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "message": "알림 설정이 업데이트되었습니다"
}
```

---

## 7. 카테고리 (Categories)

### 7.1 카테고리 목록 조회
```
GET /categories
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    { "value": "일상", "emoji": "☀️", "color": { "from": "#eab308", "to": "#f97316" } },
    { "value": "음식", "emoji": "🍕", "color": { "from": "#f97316", "to": "#ef4444" } },
    { "value": "패션", "emoji": "👕", "color": { "from": "#8b5cf6", "to": "#ec4899" } },
    { "value": "게임", "emoji": "🎮", "color": { "from": "#3b82f6", "to": "#14b8a6" } },
    { "value": "아이돌", "emoji": "⭐", "color": { "from": "#ec4899", "to": "#8b5cf6" } },
    { "value": "학교", "emoji": "🏫", "color": { "from": "#3b82f6", "to": "#8b5cf6" } },
    { "value": "영화/드라마", "emoji": "🎬", "color": { "from": "#ef4444", "to": "#8b5cf6" } },
    { "value": "운동", "emoji": "⚽", "color": { "from": "#14b8a6", "to": "#3b82f6" } },
    { "value": "취미", "emoji": "🎨", "color": { "from": "#8b5cf6", "to": "#3b82f6" } },
    { "value": "밈/유머", "emoji": "😂", "color": { "from": "#ec4899", "to": "#f97316" } },
    { "value": "환경", "emoji": "🌱", "color": { "from": "#10b981", "to": "#84cc16" } }
  ]
}
```

---

## 8. 검색 (Search)

### 8.1 투표 검색
```
GET /search/votes
```

**Query Parameters**
- `q` (required): 검색 키워드
- `type` (optional): 투표 타입 필터
- `category` (optional): 카테고리 필터
- `limit` (optional): 조회할 투표 개수 (기본값: 20)
- `offset` (optional): 페이지네이션 오프셋 (기본값: 0)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "vote_1",
      "type": "balance",
      "title": "파인애플 피자 호 VS 불호",
      "category": "음식",
      "totalVotes": 7000,
      "isHot": true,
      "matchScore": 0.95
    }
  ],
  "pagination": {
    "total": 15,
    "limit": 20,
    "offset": 0,
    "hasMore": false
  }
}
```

---

## 9. 학교 (Schools)

### 9.1 학교 목록 조회
```
GET /schools
```

**Query Parameters**
- `search` (optional): 학교명 검색
- `region` (optional): 지역 필터 (예: `서울`, `경기` 등)

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": "school_1",
      "name": "서울고등학교",
      "region": "서울",
      "totalMembers": 342,
      "totalPoints": 45280,
      "rank": 1
    },
    {
      "id": "school_2",
      "name": "강남고등학교",
      "region": "서울",
      "totalMembers": 318,
      "totalPoints": 42150,
      "rank": 2
    }
  ]
}
```

---

### 9.2 학교 상세 정보
```
GET /schools/{schoolId}
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "id": "school_1",
    "name": "서울고등학교",
    "region": "서울",
    "totalMembers": 342,
    "totalPoints": 45280,
    "rank": 1,
    "topVotes": [
      {
        "id": "vote_uniform",
        "title": "겨울 교복보다 하복이 더 예쁘다",
        "totalVotes": 900
      }
    ],
    "topMembers": [
      {
        "username": "투표왕",
        "points": 2850
      }
    ]
  }
}
```

---

## 10. 데이터 타입 정의

### VoteType
```typescript
type VoteType = "balance" | "multiple" | "ox";
```

### ActivityType
```typescript
type ActivityType = "vote" | "create" | "win" | "daily";
```

### NotificationType
```typescript
type NotificationType = "vote_trending" | "vote_ended" | "reward" | "ranking" | "new_follower";
```

### UserLevel
```typescript
type UserLevel = {
  name: string; // "브론즈", "실버", "골드", "다이아", "마스터"
  icon: string; // "🥉", "🥈", "🥇", "💎", "🏆"
  minPoints: number;
  maxPoints: number;
};
```

---

## 에러 응답 형식

모든 에러는 다음 형식으로 반환됩니다:

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "사용자 친화적 에러 메시지",
    "details": {}
  }
}
```

### 주요 에러 코드
- `AUTH_REQUIRED` (401): 인증 필요
- `FORBIDDEN` (403): 권한 없음
- `NOT_FOUND` (404): 리소스를 찾을 수 없음
- `VALIDATION_ERROR` (400): 잘못된 요청 데이터
- `ALREADY_VOTED` (409): 이미 투표한 항목
- `INSUFFICIENT_POINTS` (400): 포인트 부족
- `RATE_LIMIT_EXCEEDED` (429): 요청 제한 초과
- `SERVER_ERROR` (500): 서버 내부 오류

---

## 포인트 시스템

### 포인트 획득 방법
- 투표 참여: +1P
- 투표 생성: +2P
- 출석 체크: +5P
- 학교 인증: +50P (1회)
- 정답 맞춤 (예측 투표): +10~15P
- 연속 출석 (7일): +10P 보너스
- 주간 랭킹 보상: 순위에 따라 차등 지급

### 하루 최대 획득 포인트
- 투표 참여: 최대 20회 (20P)
- 투표 생성: 최대 5회 (10P)
- 출석 체크: 1회 (5P)
- **하루 최대: 약 35P + 보너스**

---

## 레벨 시스템

| 레벨 | 아이콘 | 최소 포인트 | 최대 포인트 |
|------|--------|-------------|-------------|
| 브론즈 | 🥉 | 0 | 499 |
| 실버 | 🥈 | 500 | 1,499 |
| 골드 | 🥇 | 1,500 | 2,999 |
| 다이아 | 💎 | 3,000 | 4,999 |
| 마스터 | 🏆 | 5,000+ | - |

---

## Rate Limiting

- 일반 요청: 100 requests/minute
- 투표 생성: 5 requests/day
- 투표 참여: 100 requests/day
- 검색: 30 requests/minute

---

## Webhook Events (선택사항)

서버에서 클라이언트로 실시간 이벤트 전달:

### 1. 투표 종료
```json
{
  "event": "vote.ended",
  "data": {
    "voteId": "vote_1",
    "results": [...]
  }
}
```

### 2. 새 HOT 투표
```json
{
  "event": "vote.trending",
  "data": {
    "voteId": "vote_new",
    "title": "새로운 핫 투표!"
  }
}
```

### 3. 랭킹 변동
```json
{
  "event": "ranking.updated",
  "data": {
    "newRank": 5,
    "oldRank": 6,
    "points": 1850
  }
}
```

---

## 버전 정보
- API Version: v1
- Last Updated: 2025-11-19
- Status: Production Ready

---

## 참고사항

1. **이미지 업로드**: `/votes` POST 요청의 `options.image`는 Base64 인코딩된 이미지 또는 이미지 URL 지원
2. **타임존**: 모든 시간은 UTC 기준 (ISO 8601 형식)
3. **페이지네이션**: 최대 100개까지 한 번에 조회 가능
4. **투표 기간**: 최소 1시간, 최대 7일
5. **학교 인증**: 이메일/SMS 인증 또는 학생증 사진 업로드

---

## 개발 환경 Base URL
- Development: `https://dev-api.picknic.app/v1`
- Staging: `https://staging-api.picknic.app/v1`
- Production: `https://api.picknic.app/v1`
