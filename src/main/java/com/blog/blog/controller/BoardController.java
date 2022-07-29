package com.blog.blog.controller;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.Reply;
import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.service.BoardService;
import com.blog.blog.service.ReplyService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final HttpSession session;
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/create")
    public String boardForm(){
        return "board/boardForm";
    }

    @PostMapping("/create")
    public String createBoard(BoardDto boardDto){
        boardDto.setWriter((User) session.getAttribute("user"));
        boardService.save(boardDto);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String board(@PathVariable Integer id, Model model){
        Board board = boardService.getBoardById(id);
        boardService.addBoardCount(id);
        model.addAttribute("board", board);

        return "board/board";
    }


    @PostMapping("/reply")
    @ResponseBody
    public String addReply(@RequestBody()ReplyDto replyDto){
        User user = (User) session.getAttribute("user");
        Board boardById = boardService.getBoardById(replyDto.getBoardId());

        replyService.addReply(replyDto, user, boardById);

        return "OK";
    }
}
