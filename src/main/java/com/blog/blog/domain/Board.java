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
    @Id @GeneratedValue
    private Integer id;

    private String title;
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Integer count; //조회수

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "board")
    private List<reply> replies = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

}
