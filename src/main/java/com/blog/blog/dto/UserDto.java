package com.blog.blog.dto;

import com.blog.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String password;


    public User createUser(){
        return new User(username, email, password);
    }
}
