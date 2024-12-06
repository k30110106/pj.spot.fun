package com.spot.fun.usr.signup.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usr/oauth")
public class OAuthSessionController {
  @GetMapping("/get-oauth-session")
  public Map<String, Object> getOAuthSessionData(HttpServletRequest request) {
    // 세션에서 데이터 가져오기
    Map<String, Object> oauthAttributes = (Map<String, Object>) request.getSession().getAttribute("oauthAttributes");
    String registrationId = (String) request.getSession().getAttribute("registrationId");
    String email = (String) request.getSession().getAttribute("email");
    String name = (String) request.getSession().getAttribute("name");
    String nickname = (String) request.getSession().getAttribute("nickname");

    return Map.of(
            "registrationId", registrationId,
            "email", email,
            "name", name,
            "nickname", nickname,
            "oauthAttributes", oauthAttributes
    );
  }
}
