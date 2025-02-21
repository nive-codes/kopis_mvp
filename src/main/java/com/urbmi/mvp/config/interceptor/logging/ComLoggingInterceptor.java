package com.urbmi.mvp.config.interceptor.logging;

import com.urbmi.mvp.common.util.ComOsIpUtil;
import com.urbmi.mvp.config.interceptor.logging.domain.ComLoggingDomain;
import com.urbmi.mvp.config.interceptor.logging.mapper.ComLoggingMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * @author nive
 * @class LoggingInterceptor
 * @since 2025-01-31
 * @desc 접속 로그를 관리하는 interceptor
 */
@Slf4j
@Component
public class ComLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private final ComLoggingMapper ComLoggingMapper;

    public ComLoggingInterceptor(ComLoggingMapper comLoggingMapper) {
        ComLoggingMapper = comLoggingMapper;
    }

    private long startTime;
    private String sessionId;

    // 요청 처리 전에 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime = System.currentTimeMillis(); // 요청 시작 시간 기록
        sessionId = request.getSession().getId();
        return true;
    }

    // 요청 처리 후 실행 (뷰 처리가 끝난 후 실행됨)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 이 부분에서는 처리 시간이 아닌, 요청에 대한 다른 정보를 준비해둘 수 있음
    }

    // 요청 완료 후 실행 (여기서 처리 시간을 계산)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 처리 시간 계산
        long processingTime = System.currentTimeMillis() - startTime;

        // 요청 데이터를 수집
        String requestUri = request.getRequestURI();
        String ipAddress = ComOsIpUtil.getIpAddr(request);  // IP 추출
        String userAgent = request.getHeader("User-Agent");
        String osInfo = ComOsIpUtil.getOsInfo(userAgent);  // OS 정보 추출
        String queryString = request.getQueryString();
        String requestMethod = request.getMethod();
        int statusCode = response.getStatus();
        String headers = getRequestHeaders(request);

        // TODO 로그인 사용자 정보 (선택 사항)

        // RequestLog 객체 생성
        ComLoggingDomain comLoggingDomain = ComLoggingDomain.builder()
                .requestTime(startTime)  // 요청이 들어왔던 시점의 시간
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .osInfo(osInfo)  // OS 정보 추가
                .requestUrl(requestUri)
                .queryString(queryString)
                .requestMethod(requestMethod)
                .statusCode(String.valueOf(statusCode))
                .processingTime(processingTime)
                .sessionId(sessionId)
                .headers(headers)
                .build();

        // DB에 저장
        ComLoggingMapper.insertLog(comLoggingDomain);

        // 로그로 출력 (필요 시)
//        log.info("Request Log: {}", comLoggingDomain);
    }
    // 요청 헤더 정보를 가져오는 유틸리티 메서드
    private String getRequestHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("; ");
        }
        return headers.toString();
    }



}
