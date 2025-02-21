package com.urbmi.mvp.config.interceptor.logging.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComLoggingDomain {
    private Long logId;                // log_id: 로그 고유 ID
    private Long requestTime;          // request_time: 요청 시간 (밀리초로 저장)
    private String ipAddress;          // ip_address: 클라이언트 IP 주소
    private String osInfo;             // os info
    private String userAgent;          // user_agent: 사용자 에이전트 (브라우저 정보 등)
    private String requestUrl;         // request_url: 요청 URL
    private String queryString;        // query_string: 쿼리 스트링
    private String requestMethod;      // request_method: HTTP 요청 방식 (GET, POST 등)
    private String statusCode;         // status_code: HTTP 상태 코드
    private Long processingTime;       // processing_time: 요청 처리 시간 (밀리초)
    private String sessionId;          // session_id: 세션 ID (사용자 세션 정보)
    private String userId;             // user_id: 사용자 ID
    private String headers;            // headers: 요청 헤더 (필요시 JSON 형식으로 저장)
    private LocalDateTime crtDt;       // crt_dt: 생성 시간 (LocalDateTime으로 변경)
}