package com.urbmi.mvp.config.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author nive
 * @class ApiResponse
 * @desc 예외 / 성공의 하나의 Response 생성
 * Error와 success했을 때의 공통(일관성)을 유지하기 위한 Response 객체 생성
 * @since 2025-01-14
 */

@AllArgsConstructor
@Getter
@Schema(description = "API 응답 모델")
public class ApiResponse<T> {
    @Schema(description = "응답 코드", example = "SUCCESS")
    private final String code;
    @Schema(description = "응답 메세지(커스텀될 수 있음)", example = "SUCCESS")
    private final String message;
    @Schema(description = "응답 데이터", example = "{test : 1}")
    private final T data;

//    private ApiResponse(String code, String message, T data) {
//        this.code = code;
//        this.message = message;
//        this.data = data;
//    }

    // 성공 응답 생성
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), null);
    }


    // 실패 응답 생성
    public static <T> ApiResponse<T> fail(ApiCode apiCode) {
        return new ApiResponse<>(apiCode.getCode(), apiCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(ApiCode apiCode, String message) {
        return new ApiResponse<>(ApiCode.VALIDATION_FAILED.getCode(), message, null);
    }

    public static <T> ApiResponse<T> fail(ApiCode apiCode, T data) {
        return new ApiResponse<>(apiCode.getCode(), apiCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(String code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}