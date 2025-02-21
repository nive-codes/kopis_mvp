package com.urbmi.mvp.common.file.service;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author nive
 * @class FileLocalUploadService
 * @desc Upload 가능한 파일의 확장자 체크 타입
 * @ComFileService.checkValidation에서 검증
 * @since 2025-01-17
 */
@Schema(description = "00-1. 파일 validation 체크용 타입 정보 - ComFileTempDomain의 fileType과 연계정보")
public enum ComFileType {
    @Schema(description = "이미지 파일 - IMAGE인 경우 해당 파일만 업로드 가능", example = "jpg, jpeg, png, gif, bmp")
    IMAGE(Set.of("jpg", "jpeg", "png", "gif", "bmp")),

    @Schema(description = "문서 파일", example = "pdf, doc, docx, txt, hwp, hwpx, xls, xlsx")
    DOCUMENT(Set.of("pdf", "doc", "docx", "txt", "hwp", "hwpx", "xls", "xlsx")),

    @Schema(description = "비디오 파일", example = "mp4, avi, mkv")
    VIDEO(Set.of("mp4", "avi", "mkv")),

    @Schema(description = "오디오 파일", example = "mp3, wav, aac")
    AUDIO(Set.of("mp3", "wav", "aac"));

    /*list보다 속도가 더 빠른 Set. 동일하게 Collcations 인터페이스를 상속받지만 저장 방식 및 동작 상이*/
    /*중복 방지*/
    /*일정한 처리 시간*/
    private final Set<String> allowedExtensions;
    private static final Map<String, ComFileType> extensionMap = new HashMap<>();

    static {
        for (ComFileType fileType : values()) {
            for (String ext : fileType.allowedExtensions) {
                extensionMap.put(ext.toLowerCase(), fileType);
            }
        }
    }

    ComFileType(Set<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    public Set<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public static ComFileType getFileTypeByExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return null; // 확장자가 없을 경우 null 반환
        }
        return extensionMap.get(extension.toLowerCase());
    }

    public static void addCustomFileType(String extension, ComFileType fileType) {
        if (extension != null && !extension.isBlank()) {
            extensionMap.put(extension.toLowerCase(), fileType);
        }
    }
}