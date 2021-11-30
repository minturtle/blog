package com.blog.blog.controller;


import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final HttpSession session;
    private final BoardService boardService;


    @GetMapping({"/", ""})
    public String indexPage(@RequestParam(defaultValue = "1") Integer page, Model model){
        List<BoardPreviewDto> boardPreviewDtos = boardService.getBoardsByPage(--page);
        int pageCount = boardService.getBoardCount() % 5 == 0 ? boardService.getBoardCount()/5: boardService.getBoardCount()/5 + 1;


        model.addAttribute("boards", boardPreviewDtos);
        model.addAttribute("pageCount", pageCount);

        return "index";
    }

}
