package com.urbmi.mvp.common.file.controller;

import com.urbmi.mvp.common.file.dto.ComFileTempDomainRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nive
 * @class ComFileTempTestController
 * @desc temp 파일 업로드 비동기 테스트 뷰(dropzone)
 * @since 2025-01-23
 */

@Controller
@RequestMapping("/tempFiles")
public class ComFileTempViewController {


    @GetMapping("/dropZoneTest")
    public String dropZoneTest(Model model) {
        //   파일 업로드 제한 요건 처리
        ComFileTempDomainRequest fileUploadInfo = ComFileTempDomainRequest.builder()
                .fileType("IMAGE")
                .fileSize(10L)
                .filePath("tempTest").build();

        model.addAttribute("fileUploadInfo", fileUploadInfo);
        return "tempFiles/dropZoneTest";
    }
}
