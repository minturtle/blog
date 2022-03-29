package com.blog.blog.service;

import com.blog.blog.domain.User;
import com.blog.blog.security.OAuthUserParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final OAuthUserParser oAuthUserParser;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        if(isNotJoinedUser(oAuth2User)){
            User user = oAuthUserParser.getUser(oAuth2User);
            userService.join(user);
        }

        return oAuth2User;
    }

    private boolean isNotJoinedUser(OAuth2User oAuth2User) {
        return userService.findById(Long.parseLong(oAuth2User.getName())) == null;
    }
}
