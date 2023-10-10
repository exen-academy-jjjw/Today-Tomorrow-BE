package com.ezen.jjjw.service;

import com.ezen.jjjw.domain.entity.BkBoard;
import com.ezen.jjjw.domain.entity.Member;
import com.ezen.jjjw.dto.request.BkBoardReqDto;
import com.ezen.jjjw.dto.request.BkBoardUpdateReqDto;
import com.ezen.jjjw.exception.CustomExceptionHandler;
import com.ezen.jjjw.jwt.TokenProvider;
import com.ezen.jjjw.repository.BkBoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : com.ezen.jjjw.service
 * fileName       : BkBoardServiceTest
 * author         : wldk9
 * date           : 2023-10-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-10        sonjia       최초 생성
 */
@ExtendWith(MockitoExtension.class)
public class BkBoardServiceTest {

    @InjectMocks
    private BkBoardService bkBoardService;

    @Mock
    private BkBoardRepository bkBoardRepository;

    @Mock
    private CustomExceptionHandler customExceptionHandler;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void success_create() {
        //given
        Member member = new Member();

        BkBoardReqDto bkBoardReqDto = new BkBoardReqDto();
        bkBoardReqDto.setPostId(1L);
        bkBoardReqDto.setCategory("study");
        bkBoardReqDto.setTitle("title");
        bkBoardReqDto.setContent("content");
        bkBoardReqDto.setCompletion(0);
        bkBoardReqDto.setShare(0);

        //stub
        when(bkBoardRepository.save(any())).thenReturn(new BkBoard());

        //when
        ResponseEntity<Long> responseEntity = bkBoardService.create(bkBoardReqDto, member);

        //then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1L, responseEntity.getBody());

        //verify
        verify(bkBoardRepository, timeout(1)).save(any());
    }

    @Test
    void success_update() {
        //given
        Long postId = 1L;
        BkBoardUpdateReqDto bkBoardUpdateReqDto = new BkBoardUpdateReqDto();
        bkBoardUpdateReqDto.setCategory("study");
        bkBoardUpdateReqDto.setTitle("title");
        bkBoardUpdateReqDto.setContent("content");
        bkBoardUpdateReqDto.setCompletion(0);
        bkBoardUpdateReqDto.setShare(0);

        Member member = Member.builder()
                .id(1L)
                .memberId("testUser")
                .nickname("testNickname")
                .password(passwordEncoder.encode("password")).build();

        BkBoard bkBoard = BkBoard.builder()
                .postId(postId)
                .member(member)
                .category("study")
                .title("title")
                .content("testContent")
                .build();

        //stub
        when(bkBoardRepository.findById(postId)).thenReturn(Optional.of(bkBoard));
        when(bkBoardRepository.save(any())).thenReturn(new BkBoard());

        //when
        ResponseEntity<Integer> responseEntity = bkBoardService.update(postId, bkBoardUpdateReqDto, member);

        //then
        assertEquals(200, responseEntity.getStatusCodeValue());

        //verify
        verify(bkBoardRepository, times(1)).findById(postId);
        verify(bkBoardRepository, times(1)).save(any());
    }

    @Test
    void fail_update_cause_author() {
        //given
        Long postId = 1L;
        BkBoardUpdateReqDto bkBoardUpdateReqDto = new BkBoardUpdateReqDto();
        bkBoardUpdateReqDto.setCategory("study");
        bkBoardUpdateReqDto.setTitle("title");
        bkBoardUpdateReqDto.setContent("content");
        bkBoardUpdateReqDto.setCompletion(0);
        bkBoardUpdateReqDto.setShare(0);

        Member member1 = Member.builder()
                .id(1L)
                .memberId("testUser")
                .nickname("testNickname")
                .password(passwordEncoder.encode("password")).build();

        Member member2 = Member.builder()
                .id(2L)
                .memberId("testUser2")
                .nickname("testNickname2")
                .password(passwordEncoder.encode("password")).build();

        BkBoard bkBoard = BkBoard.builder()
                .postId(postId)
                .member(member1)
                .category("study")
                .title("title")
                .content("testContent")
                .build();

        //stub
        when(bkBoardRepository.findById(postId)).thenReturn(Optional.of(bkBoard));

        //when
        ResponseEntity<Integer> responseEntity = bkBoardService.update(postId, bkBoardUpdateReqDto, member2);

        //then
        System.out.println("response확인 " + responseEntity);
//        assertEquals(400, responseEntity.getStatusCodeValue());

        //verify
        verify(bkBoardRepository, times(1)).findById(postId);
    }
}
