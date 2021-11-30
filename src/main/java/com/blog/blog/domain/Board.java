package com.blog.blog.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board {
    public Board() {
        this.count = 0;
    }

    public Board(String title, String content, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.count = 0;
    }

    @Id @GeneratedValue
    private Integer id;

    private String title;
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    private Integer count; //조회수

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer=" + writer.getUsername() +
                '}';
    }
}
