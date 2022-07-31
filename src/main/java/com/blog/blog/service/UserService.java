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

    /*
    * 회원가입을 진행하는 메서드
    *
    * exception : 1에서 확인 중 잘못된 것이 있다면 UserJoinFailureException을 발생시킨다.
    *
    * input : UserDTO 객체
    * 1, 회원가입에 필요한 정보들이 모두 들어오고, 중복되는 ID는 없는지 확인한다.
    * 2, UserDTO로 USER객체를 만들어 저장한다.
    * */
    public void join(UserDto userDto) throws UserJoinFailureException{
        //1
        userJoinValidate(userDto); //throwable UserJoinFailureException

        //2
        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);

    }

    /*
    * 로그인을 수행하는 메서드
    *
    * exception: 아이디, 비밀번호가 틀리면 LoginFailureException을 발생시킨다.
    *
    * 1, userDTO를 통해 유저를 찾는다.
    * 2, 1에서 찾은 user객체와 입력받은 userDTO의 password 값을 비교한다.
    * 3, 2에서 password가 일치한다면 찾은 user 객체를 리턴한다.
    * */
    @Transactional(readOnly = true)
    public User login(UserDto userDto) throws LoginFailureException{
        //1
        User findUser = userRepository.findByUsername(userDto.getUsername());
        if(findUser == null){
            throw new LoginFailureException("존재하지 않는 아이디입니다.");
        }

        //2
        else if(!isValidPassword(userDto, findUser)){
            throw new LoginFailureException("잘못된 비밀번호 입니다.");
        }
        //3
        return findUser;
    }

    /*
    * 회원가입이 가능한 입력인지 확인한다.
    * 회원가입이 가능한 조건:
    * 1, userDTO의 값이 모두 들어와 있어야함.
    * 2, username, email은 서버 내에서 유일해야함.
    * 3, 비밀번호는 4글자 이상이여야함.
    * */
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

    //userDTO와 user객체의 password가 일치한지 확인한다.
    private boolean isValidPassword(UserDto userDto, User findUser) {
        return findUser.getPassword().equals(userDto.getPassword());
    }
}
