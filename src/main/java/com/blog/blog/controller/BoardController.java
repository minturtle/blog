package com.blog.blog.controller;

import com.blog.blog.domain.Board;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/create")
    @ResponseBody
    public HttpEntity<Map<String, String>> createBoard(@RequestBody BoardDto boardDto){
        boardService.save(boardDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
