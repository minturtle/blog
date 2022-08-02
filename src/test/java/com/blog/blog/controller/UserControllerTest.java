package com.blog.blog.controller;

import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.repository.UserRepository;
import com.blog.blog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = UserController.class ,excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("get joinform test")
    void t1() throws Exception {
        //given
        //when
        ResultActions res = mockMvc.perform(get("/user/join"));
        //then
        res.andExpect(view().name("user/joinForm"));

    }

    @Test
    @DisplayName("회원가입 테스트")
    void t2() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "11111");
        String userDTOJSON = objectMapper.writeValueAsString(userDto);


        //when
        ResultActions res = mockMvc.perform(post("/user/join").contentType(MediaType.APPLICATION_JSON).content(userDTOJSON));
        //then
        res.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("회원가입 테스트 : 실패(원인: 비밀번호 자릿수)")
    void t7() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "111");
        String userDTOJSON = objectMapper.writeValueAsString(userDto);

        given(userRepository.findByUsername("root")).willReturn(null);
        given(userRepository.findByEmail("a@naver.com")).willReturn(null);

        //when
        ResultActions res = mockMvc.perform(post("/user/join").contentType(MediaType.APPLICATION_JSON).content(userDTOJSON));

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("회원가입 테스트 : 실패(원인: 이미 가입된 이메일)")
    void t8() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "1aa11");
        String userDTOJSON = objectMapper.writeValueAsString(userDto);


        given(userRepository.findByUsername("root")).willReturn(new User("root", "a@naver.com", "1aa11"));
        //when
        ResultActions res = mockMvc.perform(post("/user/join").contentType(MediaType.APPLICATION_JSON).content(userDTOJSON));
        //then
        res.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("회원가입 테스트 : 실패(원인: 이미 가입된 아이디(USERNAME))")
    void t9() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "11aa1");
        String userDTOJSON = objectMapper.writeValueAsString(userDto);


        given(userRepository.findByUsername("root")).willReturn(new User("root", "a@naver.com", "11aa1"));
        //when
        ResultActions res = mockMvc.perform(post("/user/join").contentType(MediaType.APPLICATION_JSON).content(userDTOJSON));
        //then
        res.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("get login Form Test")
    void t3() throws Exception {
        //given

        //when
        ResultActions res = mockMvc.perform(get("/user/login"));
        //then
        res.andExpect(view().name("user/loginForm"));
    }

    @Test
    @DisplayName("로그인 테스트")
    void t4() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "1111");
        User user = new User("root", "a@naver.com", "1111");

        given(userRepository.findByUsername(userDto.getUsername())).willReturn(user);
        //when
        ResultActions res = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then
        res.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().sessionAttribute("user", user));
    }

    @Test
    @DisplayName("로그인 테스트 : 아이디 찾기 실패")
    void t5() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "1111");

        given(userRepository.findByUsername(userDto.getUsername())).willReturn(null);
        //when
        ResultActions res = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("로그인 테스트 : 비밀번호 찾기 실패")
    void t6() throws Exception {
        //given
        UserDto userDto = new UserDto("root", "a@naver.com", "1111");
        User user = new User("root", "a@naver.com", "1a111");

        given(userRepository.findByUsername(userDto.getUsername())).willReturn(user);
        //when
        ResultActions res = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}