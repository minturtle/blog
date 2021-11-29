package com.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BoardPreviewDto {
    private Integer id;
    private String title;
    private String writerName;
    private Timestamp createdAt;
}
