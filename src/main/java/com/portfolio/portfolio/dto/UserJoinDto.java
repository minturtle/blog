package com.portfolio.portfolio.dto;

import com.portfolio.portfolio.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDto {
    private String username;
    private String email;
    private String password;


    public User createUser(){
        return new User(username, email, password);
    }
}
