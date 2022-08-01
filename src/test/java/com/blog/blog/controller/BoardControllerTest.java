package com.blog.blog.controller;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.service.BoardService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Controller, ControllerAdvice, Filter이 등록됨.
@WebMvcTest(value = BoardController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc //MockMVC를 IoC에 등록해줌.
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //IoC환경에서 실제 bean대신 들어감.
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("save 테스트")
    void t1() throws Exception {
        //given
        MockHttpSession httpSession = new MockHttpSession();

        BoardDto boardDto = new BoardDto("title", "content1");
        String dtoJSON = objectMapper.writeValueAsString(boardDto);
        User user = new User("root", "a2@naver.com", "1111");
        httpSession.setAttribute("user", user);

        //when
        ResultActions action = mockMvc.perform(post("/board/create")
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJSON).accept(MediaType.ALL));

        //then
        action.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
    }


    @Test
    @DisplayName("boardForm GET 테스트")
    void t2() throws Exception{
        //given
        //when
        ResultActions res = mockMvc.perform(get("/board/create"));
        //then
        res.andExpect(view().name("board/boardForm"));
    }

    @Test
    @DisplayName("board id로 조회하기 테스트")
    void t3() throws Exception {
        //given
        Board board = new Board("title", "test content", new User("root", "2@nav.com", "pw"));
        int boardId = 1;
        board.setId(1);
        given(boardService.getBoardById(boardId)).willReturn(board);
        //when
        ResultActions res = mockMvc.perform(get("/board/" + boardId));
        //then
        res.andExpect(model().attribute("board", board)).andExpect(view().name("board/board"));
    }

    @Test
    @DisplayName("없는 board 조회하기 테스트")
    void t4() throws Exception {
        //given
        int boardId = 1;
        given(boardService.getBoardById(boardId)).willThrow(IllegalArgumentException.class);
        //when
        ResultActions res = mockMvc.perform(get("/board/" + boardId));
        //then
        res.andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("댓글 달기 테스트")
    void t5() throws Exception {
        //given
        MockHttpSession httpSession = new MockHttpSession();
        int boardId = 1;
        User user = new User("root", "a2@naver.com", "1111");
        Board board = new Board("title", "con", user);
        ReplyDto replyDto = new ReplyDto("Hello reply", boardId);
        String replyDtoJson = objectMapper.writeValueAsString(replyDto);

        board.setId(1);
        httpSession.setAttribute("user", user);
        //when
        ResultActions res = mockMvc.perform(post("/board/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(replyDtoJson).session(httpSession).accept(MediaType.ALL));
        //then
        res.andExpect(status().isOk());
    }
}