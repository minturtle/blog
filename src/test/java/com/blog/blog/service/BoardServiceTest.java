package com.blog.blog.service;

import static org.mockito.BDDMockito.given;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.User;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.repository.BoardRepository;
import com.blog.blog.repository.ReplyRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks // @Mock이 달린 객체들을 주입 받기 위해 사용
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    ReplyRepository replyRepository;


    @Test
    @DisplayName("getBoardId : board 조회 성공")
    void t1(){
        //set board
        int boardId = 1;
        User user = new User("user1", "a@naver.com", "pww");
        Board board = new Board("aa", "bb",user);
        board.setId(boardId);

        //set boardRepository mock
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        assertThat(boardService.getBoardById(boardId)).isEqualTo(board);
    }

    @Test
    @DisplayName("getBoardId : board 조회 실패")
    void t2(){
        int boardId = 3;
        //boardRepository mock setting
        given(boardRepository.findById(boardId)).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(()->{
           boardService.getBoardById(boardId);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getBoardCount")
    void t3(){
        //set boardRepository mock
        given(boardRepository.findAll()).willReturn(List.of(new Board(), new Board(),new Board(),new Board(),new Board()));

        assertThat(boardService.getBoardCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("addBoardCount : board 조회 성공")
    void t4(){
        //set board
        int boardId = 1;
        User user = new User("user1", "a@naver.com", "pww");
        Board board = new Board("aa", "bb",user);
        board.setId(boardId);

        //set boardRepository mock
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        for(int i = 0; i < 3; i++) boardService.addBoardCount(boardId);

        assertThat(board.getCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("addBoardCount : board 조회 실패")
    void t5(){

        int boardId = 5;
        //set boardRepository mock
        given(boardRepository.findById(boardId)).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(()->{
           boardService.addBoardCount(boardId);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getMaxPage : 저장된 board가 0개일 때")
    void t6(){
        //set boardRepository mock
        given(boardRepository.findAll()).willReturn(List.of());

        assertThat(boardService.getMaxPage()).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 1개일 때")
    void t7(){
        //set boardRepository mock
        given(boardRepository.findAll()).willReturn(List.of(new Board()));

        assertThat(boardService.getMaxPage()).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 5개일 때")
    void t8(){
        //set boardRepository mock
        given(boardRepository.findAll()).willReturn(List.of(new Board(),new Board(),new Board(),new Board(),new Board()));

        assertThat(boardService.getMaxPage()).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 6개일 때")
    void t9(){
        //set boardRepository mock
        given(boardRepository.findAll()).willReturn(List.of(new Board(),new Board(),new Board(),new Board(),new Board(),new Board()));

        assertThat(boardService.getMaxPage()).isEqualTo(2);
    }

}