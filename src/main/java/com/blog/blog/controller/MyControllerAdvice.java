package com.blog.blog.controller;

import com.blog.blog.exceptions.LoginFailureException;
import com.blog.blog.exceptions.UserJoinFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler({UserJoinFailureException.class, LoginFailureException.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> userFailure(RuntimeException e){
        Map<String, String> resBody = new HashMap<>();
        resBody.put("error", e.getMessage());
        return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illlegalArgument(){
        return "redirect:/";
    }
}
