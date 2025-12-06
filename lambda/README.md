# Picknic Hot Vote Notification Lambda

AWS Lambda 함수로, SNS에서 Hot 투표 알림을 수신하여 모든 사용자에게 알림을 생성합니다.

## 배포 방법

### 1. 패키지 설치 및 압축

```bash
cd lambda
npm install
zip -r function.zip index.js node_modules/
```

### 2. Lambda 함수 생성 (AWS CLI 사용)

```bash
aws lambda create-function \
  --function-name picknic-hot-vote-notification-handler \
  --runtime nodejs20.x \
  --handler index.handler \
  --zip-file fileb://function.zip \
  --role arn:aws:iam::YOUR_ACCOUNT_ID:role/YOUR_LAMBDA_EXECUTION_ROLE
```

### 3. 환경 변수 설정

AWS Lambda 콘솔 또는 CLI를 통해 다음 환경 변수를 설정하세요:

```bash
aws lambda update-function-configuration \
  --function-name picknic-hot-vote-notification-handler \
  --environment Variables="{DB_HOST=your-rds-endpoint,DB_PORT=5432,DB_NAME=picknic,DB_USER=your-db-user,DB_PASSWORD=your-db-password}"
```

### 4. SNS 구독 설정

```bash
aws sns subscribe \
  --topic-arn arn:aws:sns:ap-northeast-2:493263630771:picknic-vote-completed \
  --protocol lambda \
  --notification-endpoint arn:aws:lambda:ap-northeast-2:YOUR_ACCOUNT_ID:function:picknic-hot-vote-notification-handler
```

### 5. SNS 호출 권한 부여

```bash
aws lambda add-permission \
  --function-name picknic-hot-vote-notification-handler \
  --statement-id sns-invoke \
  --action lambda:InvokeFunction \
  --principal sns.amazonaws.com \
  --source-arn arn:aws:sns:ap-northeast-2:493263630771:picknic-vote-completed
```

## 필수 환경 변수

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `DB_HOST` | PostgreSQL RDS 엔드포인트 | `picknicdb.cfeu0gayg6o3.ap-northeast-2.rds.amazonaws.com` |
| `DB_PORT` | PostgreSQL 포트 | `5432` |
| `DB_NAME` | 데이터베이스 이름 | `picknic` |
| `DB_USER` | 데이터베이스 사용자 | `picknicAdmin` |
| `DB_PASSWORD` | 데이터베이스 비밀번호 | `your-secure-password` |

## IAM 권한 요구사항

Lambda 실행 역할에 다음 권한이 필요합니다:

1. **CloudWatch Logs**: 로그 작성 권한
   ```json
   {
     "Effect": "Allow",
     "Action": [
       "logs:CreateLogGroup",
       "logs:CreateLogStream",
       "logs:PutLogEvents"
     ],
     "Resource": "arn:aws:logs:*:*:*"
   }
   ```

2. **VPC 액세스** (RDS가 VPC 내에 있는 경우):
   ```json
   {
     "Effect": "Allow",
     "Action": [
       "ec2:CreateNetworkInterface",
       "ec2:DescribeNetworkInterfaces",
       "ec2:DeleteNetworkInterface"
     ],
     "Resource": "*"
   }
   ```

## 로컬 테스트

테스트 이벤트 파일을 생성하여 로컬에서 테스트할 수 있습니다:

```bash
# test-event.json
{
  "Records": [
    {
      "Sns": {
        "Message": "{\"type\":\"HOT_VOTE\",\"voteId\":123,\"voteTitle\":\"테스트 투표\",\"category\":\"일상\",\"timestamp\":\"2025-12-06T10:30:00Z\"}"
      }
    }
  ]
}
```

## 모니터링

CloudWatch Logs에서 다음 로그를 확인할 수 있습니다:
- SNS 메시지 수신 로그
- 사용자 조회 및 알림 생성 로그
- 성공/실패 개수

## 문제 해결

### RDS 연결 실패
- Lambda가 RDS와 같은 VPC에 있는지 확인
- Security Group에서 Lambda → RDS 트래픽 허용 확인

### SNS 메시지 수신 실패
- SNS 구독이 정상적으로 설정되었는지 확인
- Lambda 호출 권한이 부여되었는지 확인
