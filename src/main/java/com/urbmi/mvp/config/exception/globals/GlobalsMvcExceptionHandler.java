package com.urbmi.mvp.config.exception.globals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.NoSuchElementException;

/**
 * @author nive
 * @class globalExceptionHandler
 * @desc 공통된 오류코드 처리 + all Exception 처리 포함
 * 프로파일에 구분에 따른 Mvc rest 예외 삭제(25.01.21)
 * @since 2025-01-10
 */
@ControllerAdvice
@Slf4j
public class GlobalsMvcExceptionHandler {
    // 404 Not Found: 리소스를 찾을 수 없는 경우
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(NoSuchElementException ex, Model model) {
        log.warn("리소스를 찾을 수 없습니다: {}", ex.getMessage());  // WARN 레벨로 로그
        model.addAttribute("errorMessage", "요청한 리소스를 찾을 수 없습니다.");
        return "/error/404";  // 404 페이지로 이동
    }

    // 400 Bad Request: 입력값이 잘못된 경우 (예: 유효성 검사 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
        log.warn("입력값 검증 실패: {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());  // WARN 레벨로 로그
        model.addAttribute("errorMessage", "입력값 검증 실패: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return "/error/400";  // 400 페이지로 이동
    }

    // 400 Bad Request: 요청이 잘못된 포맷으로 들어온 경우 (예: JSON 포맷 오류)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, Model model) {
        log.warn("잘못된 메시지 포맷: {}", ex.getMessage());  // WARN 레벨로 로그
        model.addAttribute("errorMessage", "요청 메시지 포맷이 올바르지 않습니다.");
        return "/error/400";  // 400 페이지로 이동
    }

    // 404 Not Found: 자원 미존재 (예: URL 경로가 틀린 경우)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(NoHandlerFoundException ex, Model model) {
        log.warn("잘못된 경로 요청: {}", ex.getMessage());  // WARN 레벨로 로그
        model.addAttribute("errorMessage", "요청한 자원을 찾을 수 없습니다.");
        return "/error/404";  // 404 페이지로 이동
    }

    // 400 Bad Request: Null 값이 잘못 사용된 경우
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNullPointerException(NullPointerException ex, Model model) {
        log.error("NullPointerException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        model.addAttribute("errorMessage", "Null 값이 처리되었습니다.");
        return "/error/400";  // 400 페이지로 이동
    }

    // 400 Bad Request: 배열 인덱스 범위 초과
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIndexOutOfBoundsException(IndexOutOfBoundsException ex, Model model) {
        log.error("IndexOutOfBoundsException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        model.addAttribute("errorMessage", "배열 또는 리스트의 인덱스 범위를 벗어났습니다.");
        return "/error/400";  // 400 페이지로 이동
    }

    // 400 Bad Request: 숫자 계산 오류 (예: 0으로 나누기)
    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArithmeticException(ArithmeticException ex, Model model) {
        log.error("ArithmeticException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        model.addAttribute("errorMessage", "산술 연산 오류가 발생했습니다.");
        return "/error/400";  // 400 페이지로 이동
    }


    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoResourceFoundException(NoResourceFoundException ex, Model model) {
        if (ex.getMessage().contains("favicon.ico")) {
//            log.warn("favicon.ico 리소스가 존재하지 않습니다. 예외를 무시합니다.");
            return null;  // 예외를 무시하고 아무것도 반환하지 않음
        }

        log.error("NoResourceFoundException 발생: {}", ex.getMessage(), ex);  // 오류 레벨로 로그 출력
        model.addAttribute("errorMessage", "리소스를 찾을 수 없습니다.");
        return "/error/400";  // 400 에러 페이지로 이동
    }

    @ExceptionHandler(TemplateInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String templateInputException(NoResourceFoundException ex, Model model) {
        log.error("TemplateInputException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error/404"; // 공통 에러 페이지
    }

    // 최종 에러 처리
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("Exception 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error/500"; // 공통 에러 페이지
    }


}
