package com.blog.blog.controller;

import com.blog.blog.domain.User;
import com.blog.blog.dto.KakaoProfile;
import com.blog.blog.dto.OAuthToken;
import com.blog.blog.dto.UserDto;
import com.blog.blog.service.UserService;
import com.blog.blog.utils.SecretDatas;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.OAEPParameterSpec;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final HttpSession session;
    private final RestTemplate rt;


    @GetMapping("/join")
    public String joinForm(){
        return "user/joinForm";
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<Map<String, String>> join(@RequestBody UserDto userDto){
        userService.join(userDto);
        Map<String, String> resBody = new HashMap<>();
        resBody.put("message", "회원가입에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }

    @GetMapping("/oauth2/code/kakao")
    public String kakaocallback(@RequestParam String code){

        //엑세스 토큰 요청 헤더, 바디 생성
        MultiValueMap<String, String> params = setAccessTokenBody(code);
        HttpHeaders headersForAccessToken = new HttpHeaders();
        headersForAccessToken.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headersForAccessToken);

        // Http 요청하기 - POST 방식으로 - 그리고 response 변수에 응답을 받음.
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //토큰 정보 받기
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(accessTokenResponse.getBody(), OAuthToken.class);
        }catch (JsonProcessingException e){ e.printStackTrace();}

        HttpHeaders headersForRequestProfile = new HttpHeaders();
        headersForRequestProfile.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headersForRequestProfile.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity kakaoRequestProfileEntity = new HttpEntity(headersForRequestProfile);
        ResponseEntity<String> requestProfile = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, kakaoRequestProfileEntity, String.class);

        KakaoProfile profile = null;
        try {
            profile = objectMapper.readValue(requestProfile.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(profile.toString());
        return "redirect:/";
    }

    private MultiValueMap<String, String> setAccessTokenBody(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 고정값
        params.add("client_id", SecretDatas.clientId);
        params.add("redirect_uri", "http://localhost:8080/user/oauth2/code/kakao");
        params.add("code", code);
        params.add("client_secret", SecretDatas.clientSecret);
        return params;
    }


    @GetMapping("/login")
    public String loginForm(){
        return "user/loginForm";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userLoginDto){
        User user = userService.login(userLoginDto);
        session.setAttribute("user", user);
        Map<String , String> resBody = new HashMap<>();
        resBody.put("message", "로그인에 성공하셨습니다.");
        return new ResponseEntity<>(resBody, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
