package com.blog.blog.controller;

import com.blog.blog.exceptions.LoginFailureException;
import com.blog.blog.exceptions.NoBoardWithIdException;
import com.blog.blog.exceptions.UserJoinFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler({UserJoinFailureException.class, LoginFailureException.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> userFailure(RuntimeException e){
        Map<String, String> resBody = new HashMap<>();
        resBody.put("err", e.getMessage());
        return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoBoardWithIdException.class)
    public String boardFailure(){
        return "redirect:/";
    }
}
