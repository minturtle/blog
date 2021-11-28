package com.blog.blog.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private HttpSession session;

    @GetMapping({"/", ""})
    public String indexPage(){
        return "index";
    }

}
