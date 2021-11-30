package com.blog.blog.controller;

import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.service.BoardService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final HttpSession session;
    private final BoardService boardService;

    @GetMapping("/create")
    public String boardForm(){
        return "/board/boardForm";
    }

    @PostMapping("/create")
    public String createBoard(BoardDto boardDto){
        boardDto.setWriter((User) session.getAttribute("user"));
        boardService.save(boardDto);

        return "redirect:/";
    }
}
