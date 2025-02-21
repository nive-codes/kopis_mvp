package com.urbmi.mvp.common.file.controller;

import com.urbmi.mvp.common.file.domain.ComFileDomain;
import com.urbmi.mvp.common.file.dto.ComFileDeleteRequest;
import com.urbmi.mvp.common.file.service.ComFileRestService;
import com.urbmi.mvp.config.response.ApiCode;
import com.urbmi.mvp.config.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author nive
 * @class ComFileRestController
 * @desc 기타 모듈에서의 본 파일 테이블과의 상호동작을 담당하는 RestController
 * @since 2025-01-16
 */

@Tag(name = "01.본 파일 관리", description = "원래 파일을 관리하는 서비스입니다. ")
@RestController
@RequestMapping("/api/files/")
@AllArgsConstructor
public class ComFileRestController  {


    private ComFileRestService comFileRestService;

    //   파일 업로드는 초기 TEMP에서 작업 된 후 COM_FILE로 이관 처리됩니다.
    /*@PostMapping("/upload")
    public ApiResponse saveFile(@RequestParam("files") MultipartFile[] files,      // 파일 배열
                                       @RequestBody ComFileTempDomain comFileTempDomain

    ) {
        return null;
    }*/

    //file ID, file seq를 파라미터로 받아서 처리
    @GetMapping("/upload/{fileId}")
    @Operation(summary = "저장된 파일 목록 조회", description = "저장된 파일 목록을 조회합니다.")
    public ApiResponse selectFileList(@PathVariable String fileId
    ) {
        return ApiResponse.ok(comFileRestService.selectFileMetaList(fileId));
    }


    //file ID, file seq를 파라미터로 받아서 처리
    @GetMapping("/upload/{fileId}/{fileSeq}")
    @Operation(summary = "저장된 파일 단일 조회", description = "저장된 파일 단일 정보를 조회합니다.")
    public ApiResponse selectFile(@PathVariable String fileId, @PathVariable Integer fileSeq

    ) {
        return ApiResponse.ok(comFileRestService.selectFileMeta(fileId, fileSeq));
    }

    //   삭제 처리
    @DeleteMapping("/upload/{fileId}")
    @Operation(summary = "저장된 파일 단일 삭제", description = "저장된 파일의 데이터를 논리 삭제합니다.")
    public ApiResponse selectFileList(@PathVariable String fileId, @RequestBody ComFileDeleteRequest comFileDeleteRequest)
     {
        comFileRestService.deleteFile(fileId, comFileDeleteRequest);
        return ApiResponse.ok(ApiCode.SUCCESS);
//        return ApiResponse.ok(comFileRestService.selectFileMetaList(fileId));
    }


    //file ID, file seq를 파라미터로 받아서 처리(파일 전달)
    @Operation(summary = "[dropzone+local] dropzone활용 시 이미지 썸네일", description = "저장된 파일 데이터를 토대로 썸네일이 생성됩니다(서버작동안함 주의)-파일리턴이므로 주의")
    @GetMapping("/thumb/dropzone/{fileId}/{fileSeq}")
    public ResponseEntity<Resource> dropzone(@PathVariable String fileId, @PathVariable Integer fileSeq

    ) {
        ComFileDomain fileDomain = comFileRestService.fileBinaryStream(fileId, fileSeq);
        return ResponseEntity.ok()
                .contentType(fileDomain.getMediaType())
                .body(fileDomain.getFileResource());

    }
}
