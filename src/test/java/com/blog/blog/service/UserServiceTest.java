package com.blog.blog.service;

import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.exceptions.LoginFailureException;
import com.blog.blog.exceptions.UserJoinFailureException;
import com.blog.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("회원가입 테스트 : 정상적인 입력")
    void t1() throws Exception {
        //given
        UserDto userDto = new UserDto("user", "user@naver.com", "abc1234");
        //when & then
        assertThatCode(()->userService.join(userDto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원가입 테스트 : 이미 있는 아이디")
    void t2() throws Exception {
        //given
        UserDto userDto = new UserDto("user", "user@naver.com", "abc1234");
        given(userRepository.findByUsername("user")).willReturn(new User("user", "user@naver.com", "abc1234"));

        //when & then
        assertThatThrownBy(()->userService.join(userDto))
                .isInstanceOf(UserJoinFailureException.class)
                .hasMessage("이미 존재하는 유저 이름입니다.");
    }

    @Test
    @DisplayName("회원가입 테스트 : 비밀번호 글자수 미달")
    void t3() throws Exception {
        //given
        UserDto userDto = new UserDto("user", "user@naver.com", "34");

        //when
        given(userRepository.findByUsername("user")).willReturn(null);

        //then
        assertThatThrownBy(()->userService.join(userDto))
                .isInstanceOf(UserJoinFailureException.class)
                .hasMessage("비밀번호는 4글자 이상이여야 합니다.");

    }

    @Test
    @DisplayName("로그인 테스트 : 정상적인 로그인")
    void t4() throws Exception {
        //given
        User user = new User("user", "user@email.com", "password");
        given(userRepository.findByUsername("user")).willReturn(user);
        UserDto userDto = new UserDto("user",null, "password");

        //when
        User foundUser = userService.login(userDto);
        //then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @DisplayName("로그인 테스트 : 잘못된 아이디 입력")
    void t5() throws Exception {
        //given
        given(userRepository.findByUsername("user")).willReturn(null);
        UserDto userDto = new UserDto("user",null, "password");

        //when & then
        assertThatThrownBy(()->userService.login(userDto))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("존재하지 않는 아이디입니다.");
    }

    @Test
    @DisplayName("로그인 테스트 : 잘못된 비밀번호 입력")
    void t6() throws Exception {
        //given
        User user = new User("user", "user@email.com", "password");
        given(userRepository.findByUsername("user")).willReturn(user);
        UserDto userDto = new UserDto("user",null, "passwordaa");

        //when & then
        //when & then
        assertThatThrownBy(()->userService.login(userDto))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("잘못된 비밀번호 입니다.");
    }
}