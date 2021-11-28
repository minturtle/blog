package com.blog.blog.service;

import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.exceptions.LoginFailureException;
import com.blog.blog.exceptions.UserJoinFailureException;
import com.blog.blog.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Test
    @DisplayName("회원 가입/로그인")
    public void 테스트1() throws Exception{
        //given
        UserDto userDto = new UserDto("username", "email", "password");
        //when
        userService.join(userDto);
        //then
        User user = userService.login(new UserDto("username", null,"password"));

        assertThat(user.getEmail()).isSameAs("email");
        assertThat(user.getUsername()).isSameAs("username");
        assertThat(user.getPassword()).isSameAs("password");
    }


    @Test
    @DisplayName("없는 유저 로그인")
    public void 테스트2() throws Exception{
        assertThatThrownBy(() -> {
            userService.login(new UserDto("username", null,"password"));
        }).isInstanceOf(LoginFailureException.class).hasMessage("존재하지 않는 아이디입니다.");

    }
    @Test
    @DisplayName("잘못된 비밀번호")
    public void 테스트3() throws Exception{
        UserDto userDto = new UserDto("username", "email", "password");
        userService.join(userDto);
        //when
        assertThatThrownBy(()->{
            User user = userService.login(new UserDto("username",null, "password1"));
        }).isInstanceOf(LoginFailureException.class).hasMessage("잘못된 비밀번호 입니다.");


    }
    @Test
    @DisplayName("회원가입 빈 값 넘겨주기")
    public void 테스트4() throws Exception{
        assertThatThrownBy(()->{
            userService.join(new UserDto(null, "email", "password"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserDto("username", null, "password"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserDto("username", "email", null));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");

    }

    @Test
    @DisplayName("중복되는 유저이름/이메일")
    public void 테스트5() throws Exception{
        userService.join(new UserDto("username", "email", "pwwwwww"));

        assertThatThrownBy(()->{
            userService.join(new UserDto("username", "e", "pwwww"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이미 존재하는 유저 이름입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserDto("userna", "email", "pwwww"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이미 존재하는 이메일입니다.");

    }

    @Test
    @DisplayName("비밀번호가 4글자 이하")
    public void 테스트6() throws Exception {
        assertThatThrownBy(() -> {
            userService.join(new UserDto("username", "email", "1"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("비밀번호는 4글자 이상이여야 합니다.");
    }
}