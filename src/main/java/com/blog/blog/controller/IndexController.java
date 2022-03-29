package com.blog.blog.controller;


import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.security.OAuthUserParser;
import com.blog.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final HttpSession session;
    private final BoardService boardService;
    private final OAuthUserParser oAuthUserParser;

    @GetMapping({"/", ""})
    public String indexPage(@RequestParam(defaultValue = "1") Integer page, Model model){
        List<BoardPreviewDto> boardPreviewDtos = boardService.getBoardsByPage(--page);
        int pageCount = boardService.getBoardCount() % 5 == 0 ? boardService.getBoardCount()/5: boardService.getBoardCount()/5 + 1;

        User user = oAuthUserParser.getUser();
        log.debug(String.valueOf(user));
        model.addAttribute("user", user);
        model.addAttribute("boards", boardPreviewDtos);
        model.addAttribute("pageCount", pageCount);
        return "index";
    }

}
