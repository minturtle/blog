package com.blog.blog.service;

import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.exceptions.LoginFailureException;
import com.blog.blog.exceptions.UserJoinFailureException;
import com.blog.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;



    public void join(UserDto userDto) throws UserJoinFailureException{
        userJoinValidate(userDto); //UserJoinFailureException 발생가능

        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);

    }


    @Transactional(readOnly = true)
    public User login(UserDto userDto) throws LoginFailureException{
        User findUser = userRepository.findByUsername(userDto.getUsername());
        if(findUser == null){
            throw new LoginFailureException("존재하지 않는 아이디입니다.");
        }
        else if(isValidPassword(userDto, findUser)){
            throw new LoginFailureException("잘못된 비밀번호 입니다.");
        }
        return findUser;
    }


    private void userJoinValidate(UserDto userDto) throws UserJoinFailureException{
        if(!StringUtils.hasText(userDto.getEmail()) || !StringUtils.hasText(userDto.getUsername()) || !StringUtils.hasText(userDto.getPassword())){
            throw new UserJoinFailureException("이메일, 유저 이름, 비밀번호는 필수 값입니다.");
        }
        else if(userRepository.findByUsername(userDto.getUsername()) != null){
            throw new UserJoinFailureException("이미 존재하는 유저 이름입니다.");
        }
        else if(userRepository.findByEmail(userDto.getEmail()) != null){
            throw new UserJoinFailureException("이미 존재하는 이메일입니다.");
        }
        else if(userDto.getPassword().length() < 4){
            throw new UserJoinFailureException("비밀번호는 4글자 이상이여야 합니다.");
        }
    }

    private boolean isValidPassword(UserDto userDto, User findUser) {
        return !findUser.getPassword().equals(userDto.getPassword());
    }
}
