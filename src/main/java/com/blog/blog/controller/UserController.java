package com.blog.blog.controller;
import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HttpSession session;
    
    
    /*
    * join Form을 보여주는 메서드
    * */
    @GetMapping("/join")
    public String joinForm(){
        return "user/joinForm";
    }


    /*
    * 사용자가 회원가입을 시도할 때 수행하는 메서드
    * 회원가입을 성공하면 성공 message와 함께 200코드가 전송되고,
    * 실패하면 MyControllerAdvice의 userFailure 메서드가 호출되어 실패 메세지가 전송된다.
    * */
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<Map<String, String>> join(@RequestBody UserDto userDto){
        userService.join(userDto);
        Map<String, String> resBody = new HashMap<>();
        resBody.put("message", "회원가입에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }


    /*
     * login Form을 보여주는 메서드
     * */
    @GetMapping("/login")
    public String loginForm(){
        return "user/loginForm";
    }


    /*
     * 사용자가 로그인을 시도할 때 수행하는 메서드
     * 로그인을 성공하면 성공 message와 함께 200코드가 전송되고,
     * 실패하면 MyControllerAdvice의 userFailure 메서드가 호출되어 실패 메세지가 전송된다.
     * */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userLoginDto){
        User user = userService.login(userLoginDto);
        session.setAttribute("user", user);
        Map<String , String> resBody = new HashMap<>();
        resBody.put("message", "로그인에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }


    /*
    * 사용자가 로그아웃을 클릭했을때 실행되는 메서드로, 세션의 user값을 제거시킨다.
    * */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        session.removeAttribute("user");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    

}
