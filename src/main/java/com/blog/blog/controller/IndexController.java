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

    /*
    * index page를 보여주는 메서드, 이 때 Board가 있다면 Board를 유저가 요청한 페이지에 맞게 조회해 리턴해야한다.
    * input: URL의 Query Parameter을 통해 page 값을 받아옴(default 1)
    *
    * 1, 5개의 boardPreviewDTO 객체 리스트를 페이징해 가져온다.
    * 2, 모델에 1에서 조회한 리스트를 담는다.
    * 3, 뷰에 전달해 리턴한다.
    * */
    @GetMapping({"/", ""})
    public String indexPage(@RequestParam(defaultValue = "1") Integer page, Model model){
        //1
        List<BoardPreviewDto> boardPreviewDtos = boardService.getBoardsByPage(page);
        int pageCount = boardService.getMaxPage();

        //2
        model.addAttribute("boards", boardPreviewDtos);
        model.addAttribute("pageCount", pageCount);

        //3
        return "index";
    }

}
