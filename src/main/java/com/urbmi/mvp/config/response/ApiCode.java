package com.urbmi.mvp.config.response;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiCode {
    // 성공 코드
    SUCCESS(HttpStatus.OK, "SUCCESS", "요청이 성공적으로 처리되었습니다."),

    // 클라이언트 오류 코드
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "입력값 검증 실패했습니다."),
    SAME_DATA(HttpStatus.BAD_REQUEST,"BAD_REQUEST","중복된 데이터가 있습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "요청 메시지 포맷이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),

    // 서버 오류 코드
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR, "NULL_POINTER", "Null 값이 처리되었습니다."),
    INDEX_OUT_OF_BOUNDS(HttpStatus.INTERNAL_SERVER_ERROR, "INDEX_OUT_OF_BOUNDS", "배열 또는 리스트의 인덱스 범위를 벗어났습니다."),
    ARITHMETIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ARITHMETIC_ERROR", "연산 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "알 수 없는 오류가 발생했습니다."),
    /*커스텀 정의 return 객체*/
    CUSTOM(HttpStatus.INTERNAL_SERVER_ERROR, "CUSTOM", "커스텀 에러 메시지");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ApiCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    // ApiCode.CUSTOM을 사용하도록 변경
    public static ApiCode custom(String code, String message) {
        return ApiCode.CUSTOM;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
