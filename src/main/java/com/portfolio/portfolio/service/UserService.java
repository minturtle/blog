package com.portfolio.portfolio.service;

import com.portfolio.portfolio.domain.User;
import com.portfolio.portfolio.dto.UserJoinDto;
import com.portfolio.portfolio.dto.UserLoginDto;
import com.portfolio.portfolio.exceptions.LoginFailureException;
import com.portfolio.portfolio.exceptions.UserJoinFailureException;
import com.portfolio.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;



    public void join(UserJoinDto userDto) throws UserJoinFailureException{
        userJoinValidate(userDto); //UserJoinFailureException 발생가능

        User user = userDto.createUser();
        userRepository.save(user);

    }


    @Transactional(readOnly = true)
    public User login(UserLoginDto userDto) throws LoginFailureException{
        User findUser = userRepository.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
        if(findUser == null){
            throw new LoginFailureException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        return findUser;
    }



    private void userJoinValidate(UserJoinDto userDto) throws UserJoinFailureException{
        if(userDto.getEmail() == null || userDto.getUsername() == null || userDto.getPassword() == null){
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
}
