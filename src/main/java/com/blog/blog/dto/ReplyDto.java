package com.blog.blog.dto;

import com.blog.blog.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private String content;
    private Integer boardId;

    @Override
    public String toString() {
        return "ReplyDto{" +
                "content='" + content + '\'' +
                ", boardId=" + boardId +
                '}';
    }

}
