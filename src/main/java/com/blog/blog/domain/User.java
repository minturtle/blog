package com.blog.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {

    public User(Long id, String username, String image, String thumbnail_image, String email) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.thumbnail_image = thumbnail_image;
        this.email = email;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Id
    private Long id;

    private String username;
    private String image;
    private String thumbnail_image;
    private String email;

    @CreationTimestamp
    private Timestamp createdAt;


}
