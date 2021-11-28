package com.blog.blog.controller;

import com.blog.blog.domain.User;
import com.blog.blog.dto.UserDto;
import com.blog.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/join")
    public String joinForm(){
        return "/user/joinForm";
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<Map<String, String>> join(@RequestBody UserDto userDto){
        userService.join(userDto);
        Map<String, String> resBody = new HashMap<>();
        resBody.put("message", "회원가입에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }


    @GetMapping("/login")
    public String loginForm(){
        return "/user/loginForm";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userLoginDto){
        User user = userService.login(userLoginDto);
        session.setAttribute("user", user);
        Map<String , String> resBody = new HashMap<>();
        resBody.put("message", "로그인에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }
}
