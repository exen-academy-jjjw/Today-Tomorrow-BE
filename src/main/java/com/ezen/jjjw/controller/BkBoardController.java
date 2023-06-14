package com.ezen.jjjw.controller;




import com.ezen.jjjw.domain.entity.BkBoard;
import com.ezen.jjjw.dto.BkBoardDto;
import com.ezen.jjjw.dto.response.ResponseDto;
import com.ezen.jjjw.service.BkBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bkBoard")
public class BkBoardController {
    private  final BkBoardService bkBoardService;


    // create
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody BkBoardDto.Request bkrequest, HttpServletRequest request) {
        Object postid = bkBoardService.create(bkrequest, request);
        return ResponseEntity.created(URI.create("/"+postid)).build();
    }


    // update
    @PutMapping("/{postId}")
    public ResponseEntity<ResponseDto<Object>> update(@PathVariable("postId") Long postId, @RequestBody BkBoardDto.Request bkrequest) {
        log.info("postId = {}", postId);
        ResponseDto<Object> response = bkBoardService.update(postId, bkrequest);
        ResponseDto<Object> responseDto = ResponseDto.success(response);
        return ResponseEntity.ok().body(responseDto);
    }


    // list
    @GetMapping("/list")
    public ResponseEntity<List<BkBoard>> getAllBkBoardDto(HttpServletRequest request){

        log.info("post count = {}", bkBoardService.getAllBkBoardDto(request).size());
        return ResponseEntity.ok().body(bkBoardService.getAllBkBoardDto(request));
    }

    // delete
    @DeleteMapping("/{postId}")
    public void delete(@PathVariable("postId") Long postId, HttpServletRequest request) {
        bkBoardService.delete(postId, request);
    }

}