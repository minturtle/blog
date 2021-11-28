package com.portfolio.portfolio.service;

import com.portfolio.portfolio.domain.User;
import com.portfolio.portfolio.dto.UserJoinDto;
import com.portfolio.portfolio.dto.UserLoginDto;
import com.portfolio.portfolio.exceptions.LoginFailureException;
import com.portfolio.portfolio.exceptions.UserJoinFailureException;
import com.portfolio.portfolio.repository.UserRepository;

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
        UserJoinDto userDto = new UserJoinDto("username", "email", "password");
        //when
        userService.join(userDto);
        //then
        User user = userService.login(new UserLoginDto("username", "password"));

        assertThat(user.getEmail()).isSameAs("email");
        assertThat(user.getUsername()).isSameAs("username");
        assertThat(user.getPassword()).isSameAs("password");
    }


    @Test
    @DisplayName("없는 유저 로그인")
    public void 테스트2() throws Exception{
        assertThatThrownBy(() -> {
            userService.login(new UserLoginDto("username", "password"));
        }).isInstanceOf(LoginFailureException.class);

    }

    @Test
    @DisplayName("회원가입 빈 값 넘겨주기")
    public void 테스트3() throws Exception{
        assertThatThrownBy(()->{
            userService.join(new UserJoinDto(null, "email", "password"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserJoinDto("username", null, "password"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserJoinDto("username", "email", null));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이메일, 유저 이름, 비밀번호는 필수 값입니다.");

    }

    @Test
    @DisplayName("중복되는 유저이름/이메일")
    public void 테스트4() throws Exception{
        userService.join(new UserJoinDto("username", "email", "pwwwwww"));

        assertThatThrownBy(()->{
            userService.join(new UserJoinDto("username", "e", "pwwww"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이미 존재하는 유저 이름입니다.");
        assertThatThrownBy(()->{
            userService.join(new UserJoinDto("userna", "email", "pwwww"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("이미 존재하는 이메일입니다.");

    }

    @Test
    @DisplayName("비밀번호가 4글자 이하")
    public void 테스트5() throws Exception {
        assertThatThrownBy(() -> {
            userService.join(new UserJoinDto("username", "email", "1"));
        }).isInstanceOf(UserJoinFailureException.class).hasMessage("비밀번호는 4글자 이상이여야 합니다.");
    }
}