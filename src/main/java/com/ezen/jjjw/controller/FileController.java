package com.ezen.jjjw.controller;

import com.ezen.jjjw.dto.response.ResponseDto;
import com.ezen.jjjw.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * packageName    : com.connectiontest.test.controller
 * fileName       : FileController.java
 * author         : won
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        won       최초 생성
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/file")
public class FileController {

    private final FileService fileService;

    // 리뷰 파일 첨부 POST /file/create/{reviewId}
    @PostMapping(value = "/create/{reviewId}")
    public ResponseDto<?> createFile(@PathVariable Long reviewId,
                                     @RequestParam(value = "fileUrl", required = false) List<MultipartFile> multipartFiles, HttpServletRequest request) {
        return fileService.createFile(reviewId, multipartFiles,  request);
    }

    // 리뷰 파일 상세 GET /file/detail/{reviewId}
    @GetMapping(value = "/detail/{reviewId}")
    public ResponseDto<?> detailFile(@PathVariable Long reviewId, HttpServletRequest request) {
        return fileService.findReviewId(reviewId, request);
    }

    // 리뷰 파일 수정 PUT /file/update/{reviewId}
    @PutMapping(value = "/update/{reviewId}")
    public ResponseDto<?> updateFile(@PathVariable Long reviewId, @RequestParam("fileUrl") List<MultipartFile> multipartFiles, HttpServletRequest request) {
        return fileService.updateFile(reviewId, multipartFiles, request);
    }

    // 리뷰 파일 삭제 POST /file/delete/{reviewId}
    @DeleteMapping(value = "/delete/{reviewId}")
    public ResponseDto<?> deleteFile(@PathVariable Long reviewId, @RequestParam("fileUrl") List<MultipartFile> multipartFiles, HttpServletRequest request) {
        return fileService.deleteByFileId(reviewId, multipartFiles, request);
    }

}