package com.blog.blog.dto;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto{
    private String title;
    private String content;

}
