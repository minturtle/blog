package com.portfolio.portfolio.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;


    @CreationTimestamp
    private Timestamp createdAt;
}
