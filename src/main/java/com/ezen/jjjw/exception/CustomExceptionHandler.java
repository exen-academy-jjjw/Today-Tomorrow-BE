package com.ezen.jjjw.exception;

import com.ezen.jjjw.domain.entity.BkBoard;
import com.ezen.jjjw.domain.entity.Member;
import com.ezen.jjjw.domain.entity.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * packageName    : com.ezen.jjjw.exception
 * fileName       : ExceptionHandler
 * author         : sonjia
 * date           : 2023-06-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-27        sonjia       최초 생성
 */
@Slf4j
@Component
public class CustomExceptionHandler {

    public ResponseEntity<Integer> getNotFoundMemberStatus(Member member) {
        if(member == null) {
            log.info("존재하지 않는 사용자");
            return ResponseEntity.ok(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    public ResponseEntity<Integer> getNotFoundBoardStatus(BkBoard bkBoard) {
        if(bkBoard == null) {
            log.info("존재하지 않는 게시글");
            return ResponseEntity.ok(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    public ResponseEntity<?> getNotFoundReviewStatusOrgetReview(BkBoard bkBoard) {
        int existReview = bkBoard.getExistReview();
        Review findReview = bkBoard.getReview();
        if (existReview == 0 || findReview == null) {
            log.info("존재하지 않는 리뷰");
            return ResponseEntity.ok(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }
}