/**
 * Picknic Hot Vote Notification Handler (AWS Lambda)
 *
 * SNS에서 Hot 투표 알림을 수신하여 모든 사용자에게 알림을 생성하는 Lambda 함수
 *
 * Environment Variables:
 * - DB_HOST: PostgreSQL RDS 호스트
 * - DB_PORT: PostgreSQL 포트 (default: 5432)
 * - DB_NAME: 데이터베이스 이름
 * - DB_USER: 데이터베이스 사용자
 * - DB_PASSWORD: 데이터베이스 비밀번호
 */

const { Client } = require('pg');

/**
 * Lambda 핸들러 함수
 */
exports.handler = async (event) => {
    console.log('Received SNS event:', JSON.stringify(event, null, 2));

    // PostgreSQL 클라이언트 설정
    const client = new Client({
        host: process.env.DB_HOST,
        port: process.env.DB_PORT || 5432,
        database: process.env.DB_NAME,
        user: process.env.DB_USER,
        password: process.env.DB_PASSWORD,
    });

    try {
        // 1. SNS 메시지 파싱
        const snsMessage = event.Records[0].Sns.Message;
        const messageData = JSON.parse(snsMessage);

        const { type, voteId, voteTitle, category } = messageData;

        console.log('Parsed message data:', messageData);

        // 타입 검증
        if (type !== 'HOT_VOTE') {
            console.log('Ignoring non-HOT_VOTE message type:', type);
            return {
                statusCode: 200,
                body: JSON.stringify({ message: 'Ignored non-HOT_VOTE message' })
            };
        }

        // 2. PostgreSQL 연결
        await client.connect();
        console.log('Connected to PostgreSQL database');

        // 3. 모든 사용자 조회
        const usersQuery = 'SELECT email FROM users';
        const usersResult = await client.query(usersQuery);
        const users = usersResult.rows;

        console.log(`Found ${users.length} users to notify`);

        // 4. 각 사용자에게 알림 생성
        const notificationTitle = '🔥 HOT';
        const notificationMessage = `${voteTitle} - ${category} 투표가 HOT으로 선정되었습니다!`;

        const insertQuery = `
            INSERT INTO notifications (user_id, type, title, message, vote_id, is_read, created_at)
            VALUES ($1, $2, $3, $4, $5, $6, NOW())
        `;

        let successCount = 0;
        let failureCount = 0;

        for (const user of users) {
            try {
                await client.query(insertQuery, [
                    user.email,
                    'HOT_VOTE',
                    notificationTitle,
                    notificationMessage,
                    voteId,
                    false
                ]);
                successCount++;
            } catch (error) {
                console.error(`Failed to insert notification for user ${user.email}:`, error);
                failureCount++;
            }
        }

        console.log(`Notifications created: ${successCount} success, ${failureCount} failures`);

        return {
            statusCode: 200,
            body: JSON.stringify({
                message: 'Notifications created successfully',
                voteId,
                usersNotified: successCount,
                failures: failureCount
            })
        };

    } catch (error) {
        console.error('Error processing SNS message:', error);
        return {
            statusCode: 500,
            body: JSON.stringify({
                message: 'Error processing notification',
                error: error.message
            })
        };
    } finally {
        // 5. 연결 종료
        await client.end();
        console.log('PostgreSQL connection closed');
    }
};
