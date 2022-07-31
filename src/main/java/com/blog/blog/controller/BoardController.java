package com.blog.blog.controller;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.Reply;
import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.service.BoardService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final HttpSession session;
    private final BoardService boardService;


    /*
    * board form을 리턴한다.
    * */
    @GetMapping("/create")
    public String boardForm(){
        return "board/boardForm";
    }

    /*
    * 1, board를 저장하기 전 필요한 User 정보를 세션으로부터 가져온다.
    * 2, board를 저장한다.
    * 3, 그 후 메인 페이지로 리다이렉트 한다.
    * */
    @PostMapping("/create")
    public String createBoard(BoardDto boardDto){
        //1
        User writer = (User) session.getAttribute("user");
        //2
        boardService.save(boardDto, writer);

        //3
        return "redirect:/";
    }

    /*
    * 1, 사용자가 찾는 board객체를 찾는다.
    * 2, board객체의 조회수(count)를 1증가 시킨다.
    * 3, board를 읽는 페이지를 리턴한다.
    * */
    @GetMapping("/{id}")
    public String board(@PathVariable Integer id, Model model){
        //1
        Board board = boardService.getBoardById(id);
        //2
        boardService.addBoardCount(id);

        //3
        model.addAttribute("board", board);
        return "board/board";
    }

    /*
    * reply을 작성하는 메서드, JS에서 JSON으로 요청받기 때문에 리턴도 JSON으로 진행한다.
    * 1, reply에 필요한 유저정보를 세션으로 부터 조회한다.
    * 2, board에 reply을 추가한다. 이때 필요한 board정보는 replyDTO에 들어있다.
    * 3, 정상적으로 reply 저장에 성공한 경우 STATUS 200을 리턴한다.
    * 4, 정상적으로 reply을 저장하지 못했다면 STATUS 400을 리턴한다.
    * */
    @PostMapping("/reply")
    @ResponseBody
    public ResponseEntity<Object> addReply(@RequestBody ReplyDto replyDto){
        User user = (User) session.getAttribute("user");
        try{
            boardService.addReply(replyDto, user);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
