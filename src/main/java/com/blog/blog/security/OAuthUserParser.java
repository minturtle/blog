package com.blog.blog.security;

import com.blog.blog.domain.User;

import com.blog.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class OAuthUserParser {

    //세션값을 읽어 유저 도메인을 반환하는 함수
    public User getUser(){
        try {
            OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(oAuth2User == null)return null;

            Map<String, String> properties = (Map<String, String>)oAuth2User.getAttribute("properties");
            User user1 = new User(Long.parseLong(oAuth2User.getName()), properties.get("nickname"), properties.get("profile_image"),
                    properties.get("thumbnail_image"), oAuth2User.getAttribute("email"));

            return user1;
        }catch(ClassCastException e){
            return null;
        }

    }

    public User getUser(OAuth2User oAuth2User){
        if(oAuth2User == null)return null;

        Map<String, String> properties = (Map<String, String>)oAuth2User.getAttribute("properties");
        User user1 = new User(Long.parseLong(oAuth2User.getName()), properties.get("nickname"), properties.get("profile_image"),
                properties.get("thumbnail_image"), oAuth2User.getAttribute("email"));
        return user1;
    }
}
