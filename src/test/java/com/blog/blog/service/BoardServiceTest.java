package com.blog.blog.service;

import static org.mockito.BDDMockito.given;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.User;

import com.blog.blog.repository.BoardRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;


//Mock 환경 안에서 돌아감.
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks // @Mock이 달린 객체들을 주입 받기 위해 사용
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Test
    @DisplayName("getBoardId : board 조회 성공")
    void t1(){
        //given
        int boardId = 1;
        User user = new User("user1", "a@naver.com", "pww");
        Board board = new Board("aa", "bb",user);
        board.setId(boardId);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        //when
        Board foundBoard = boardService.getBoardById(boardId);

        //then
        assertThat(foundBoard).isEqualTo(board);
    }

    @Test
    @DisplayName("getBoardId : board 조회 실패")
    void t2(){
        //given
        int boardId = 3;
        given(boardRepository.findById(boardId)).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(()->{
           boardService.getBoardById(boardId);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getBoardCount")
    void t3(){
        //given
        given(boardRepository.findAll()).willReturn(List.of(new Board(), new Board(),new Board(),new Board(),new Board()));

        //when
        assertThat(boardService.getBoardCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("addBoardCount : board 조회 성공")
    void t4(){
        //given
        int boardId = 1;
        User user = new User("user1", "a@naver.com", "pww");
        Board board = new Board("aa", "bb",user);
        board.setId(boardId);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        //when
        for(int i = 0; i < 3; i++) boardService.addBoardCount(boardId);

        //then
        assertThat(board.getCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("addBoardCount : board 조회 실패")
    void t5(){

        //given
        int boardId = 5;
        given(boardRepository.findById(boardId)).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(()->{
           boardService.addBoardCount(boardId);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getMaxPage : 저장된 board가 0개일 때")
    void t6(){
        //given
        given(boardRepository.findAll()).willReturn(List.of());

        //when
        int maxPage = boardService.getMaxPage();
        //then
        assertThat(maxPage).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 1개일 때")
    void t7(){
        //given
        given(boardRepository.findAll()).willReturn(List.of(new Board()));

        //when
        int maxPage = boardService.getMaxPage();
        //then
        assertThat(maxPage).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 5개일 때")
    void t8(){
        //given
        given(boardRepository.findAll()).willReturn(List.of(new Board(),new Board(),new Board(),new Board(),new Board()));

        //when
        int maxPage = boardService.getMaxPage();
        //then
        assertThat(maxPage).isEqualTo(1);
    }
    @Test
    @DisplayName("getMaxPage : 저장된 board가 6개일 때")
    void t9(){
        //given
        given(boardRepository.findAll()).willReturn(List.of(new Board(),new Board(),new Board(),new Board(),new Board(),new Board()));

        //when
        int maxPage = boardService.getMaxPage();
        //then
        assertThat(maxPage).isEqualTo(2);
    }


}