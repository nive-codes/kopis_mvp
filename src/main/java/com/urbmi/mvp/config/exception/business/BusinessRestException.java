package com.urbmi.mvp.config.exception.business;

import lombok.Getter;

/**
 * @author nive
 * @class BusinessException
 * @desc service layer의 exception을 발생 시키기 위한 공통 Exception 처리(+상태코드추가)
 * SuccessResponse에 Data객체 안에 세부코드 및 에러 메세지를 return 하는 방법도 있지만 향후 추가 예정.
 * @since 2025-01-10
 */
@Getter
public class BusinessRestException extends RuntimeException {
//    private final String errorCode;
//    private final HttpStatus httpStatus;  // HTTP 상태 코드 추가
//
//    public BusinessRestException(String message, String errorCode, HttpStatus httpStatus) {
//        super(message);
//        this.errorCode = errorCode;
//        this.httpStatus = httpStatus;
//    }


}