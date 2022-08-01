package com.blog.blog.controller;

import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.service.BoardService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BoardController.class) // Controller, ControllerAdvice, Filter이 등록됨.
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
        //403 에러 발생, 왜일까?
        action.andExpect(status().is3xxRedirection()).andExpect(content().string("redirect:/"));
    }
}