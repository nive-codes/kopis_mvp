package com.urbmi.mvp.config.exception.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author nive
 * @class globalExceptionHandler
 * @desc 비즈니스 공통 예외 처리 handler - submit일 때 다시 return view 포함 / 커s스텀 business가 너무 많아지면 관리 부하방지
 * @since 2025-01-10
 */
@ControllerAdvice
@Slf4j
public class BusinessExceptionMvcHandler {

    // 404 Not Found: 리소스를 찾을 수 없는 경우
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Model model) {
        log.error("BusinessExceptionMvcHandler.BusinessException 발생 : {} (View: {}, ErrorCode: {}, HttpStatus: {})",
                ex.getMessage(), ex.getReturnView(), ex.getErrorCode(), ex.getHttpStatus(), ex);


        // 예외 발생 시 처리할 메시지와 뷰를 설정
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());  // errorCode도 같이 전달 가능
        model.addAttribute("httpStatus", ex.getHttpStatus().value());  // HttpStatus 코드 전달

        return ex.getReturnView();  // 예외에서 지정한 뷰로 이동
    }
}
