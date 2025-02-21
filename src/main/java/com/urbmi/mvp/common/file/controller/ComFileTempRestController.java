package com.urbmi.mvp.common.file.controller;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import com.urbmi.mvp.common.file.dto.ComFileTempDeleteRequest;
import com.urbmi.mvp.common.file.dto.ComFileTempDomainRequest;
import com.urbmi.mvp.common.file.service.ComFileTempRestService;
import com.urbmi.mvp.config.response.ApiCode;
import com.urbmi.mvp.config.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author nive
 * @class ComFileTempRestController
 * @desc 타 모듈의 프론트에서 처리될 임시 파일 데이터 저장 및 파일 저장을 담당하는 controller
 * @since 2025-01-23
 */
@Tag(name = "00.임시 파일 관리", description = "임시 파일을 저장하는 서비스입니다. 임시 파일에서의 데이터를 저장 이후 각 모듈의 데이터가 저장 될 시 FILE_ID를 파라미터로 본래 파일 테이블에 이관됩니다.")
@RestController
@RequestMapping("/api/tempFiles/")
@AllArgsConstructor
public class ComFileTempRestController {


    private ComFileTempRestService comFileTempRestService;

    // 파일 업로드 시 Temp 파일에 최초 업로드
    @PostMapping("/upload")
    @Operation(summary = "임시 파일 저장", description = "임시 파일 데이터를 저장 후 파일을 저장합니다.")
    public ApiResponse saveFile(@RequestParam("files") MultipartFile[] files,      // 파일 배열
                                @Valid  ComFileTempDomainRequest request

    ) {
//        String idx = ;
        // 업로드된 파일의 정보 포함하여 응답 반환
        return ApiResponse.ok("파일 업로드 성공했습니다.",comFileTempRestService.uploadFileTemp(files,request));
    }

    //temp 파일 삭제(모듈 save 전)
    //file ID, file seq를 파라미터로 받아서 처리
    @DeleteMapping("/upload/{fileId}")
    @Operation(summary = "임시 파일 삭제", description = "파일을 삭제 후 임시 파일 데이터도 삭제합니다.")
    public ApiResponse deleteFile(@PathVariable String fileId, @RequestBody ComFileTempDeleteRequest comFileTempDeleteRequest) {

        if(Objects.isNull(fileId) || fileId.isBlank()){
            return ApiResponse.fail(ApiCode.NOT_FOUND,new ComFileTempDomain());
        }

        comFileTempRestService.deleteFileTemp(fileId, comFileTempDeleteRequest);

        return ApiResponse.ok("파일 삭제 되었습니다.");
    }


    /*TODO expired된 데이터 전체 삭제 처리 + file 데이터 전체 삭제 처리 추가 필요*/

}
