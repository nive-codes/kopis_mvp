package com.urbmi.mvp.config.exception.business;

import com.urbmi.mvp.config.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author nive
 * @class BusinessException
 * @since 2025-01-10
 * @desc service layer의 exception을 발생 시키기 위한 공통 Exception 처리
 *         - MVC 웹 응답, REST API 응답 모두 처리 가능하도록 통합(25.01.21)
 *         - 기본값 설정: HTTP 상태코드, 뷰, 에러 코드 등(25.01.21)
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String message;      // 에러 메시지 (공용)
    private final String returnView;   // 에러 처리 후 리턴할 뷰 (MVC용)  기본적으로 /error/500 활용
    private final String errorCode;    // API 응답에 사용할 에러 코드 (REST API용)
    private final HttpStatus httpStatus;  // HTTP 상태 코드 (REST API용)
    private final Object data;          // return 데이터 객체
    private final ApiCode apiCode;  //ApiCode의 집합체

    // 1. message만 있는 경우 (기본적으로 data는 null로 설정)
    public BusinessException(String message) {
        this(message, "/error/500", ApiCode.INTERNAL_SERVER_ERROR, null);  // 기본값 설정
    }

    // 2. ApiCode만 있는 경우 (기본적으로 data는 null로 설정)
    public BusinessException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.message = apiCode.getMessage();
        this.returnView = "/error/500";  // 기본 에러 뷰
        this.errorCode = apiCode.getCode();
        this.httpStatus = apiCode.getStatus();
        this.apiCode = apiCode;
        this.data = null;
    }

    // 3. message와 returnView만 있는 경우
    public BusinessException(String message, String returnView) {
        this(message, returnView, ApiCode.INTERNAL_SERVER_ERROR, null);  // 기본값 설정
    }

    // 4. message와 ApiCode만 있는 경우
    public BusinessException(String message, ApiCode apiCode) {
        this(message, "/error/500", apiCode, null);  // 기본적으로 view는 "/error/500"
    }

    // 5. message와 returnView, ApiCode만 있는 경우
    public BusinessException(String message, String returnView, ApiCode apiCode) {
        this(message, returnView, apiCode, null);  // 기본적으로 data는 null
    }

    // 6. message와 errorCode, httpStatus만 있는 경우 (기본적으로 data는 null로 설정)
    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.returnView = "/error/500";  // 기본값 설정
        this.errorCode = errorCode != null ? errorCode : "INTERNAL_SERVER_ERROR"; // 기본 에러 코드
        this.httpStatus = httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR; // 기본 상태 코드
        this.apiCode = ApiCode.INTERNAL_SERVER_ERROR;  // 기본 ApiCode
        this.data = null;  // data는 null로 설정
    }

    // 7. message, ApiCode, Object data를 함께 받는 경우 (data 추가)
    public BusinessException(String message, ApiCode apiCode, Object data) {
        this(message, "/error/500", apiCode, data);  // data를 추가하여 호출
    }

    // 8. message, returnView, ApiCode, Object data를 모두 받는 경우
    public BusinessException(String message, String returnView, ApiCode apiCode, Object data) {
        super(message);
        this.message = message;
        this.returnView = returnView != null ? returnView : "/error/500";  // 기본값 설정
        this.errorCode = apiCode.getCode();  // ApiCode에서 에러 코드 가져옴
        this.httpStatus = apiCode.getStatus();  // ApiCode에서 상태 코드 가져옴
        this.apiCode = apiCode;
        this.data = data;  // data 설정
    }

    // 9. message, errorCode, httpStatus, Object data를 받는 경우
    public BusinessException(String message, String errorCode, HttpStatus httpStatus, Object data) {
        super(message);
        this.message = message;
        this.returnView = "/error/500";  // 기본값 설정
        this.errorCode = errorCode != null ? errorCode : "INTERNAL_SERVER_ERROR"; // 기본 에러 코드
        this.httpStatus = httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR; // 기본 상태 코드
        this.apiCode = ApiCode.INTERNAL_SERVER_ERROR;  // 기본 ApiCode
        this.data = data;  // data 설정
    }
}
