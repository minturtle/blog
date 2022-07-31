package com.blog.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Reply {
    @Id @GeneratedValue
    private Integer id;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @CreationTimestamp
    private Timestamp createdAt;

    public Reply(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", board=" + board +
                ", createdAt=" + createdAt +
                '}';
    }
}
