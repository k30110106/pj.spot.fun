package com.spot.fun.usr.signup.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/usr/oauth")
public class OAuthSessionController {
  @GetMapping("/get-oauth-session")
  public  ResponseEntity<Map<String, Object>> getOAuthSessionData(HttpServletRequest request) {
    // 세션에서 데이터 가져오기
    Map<String, Object> oauthAttributes = (Map<String, Object>) request.getSession().getAttribute("oauthAttributes");
    String registrationId = (String) request.getSession().getAttribute("registrationId");
    String email = (String) request.getSession().getAttribute("email");
    String name = (String) request.getSession().getAttribute("name");
    String nickname = (String) request.getSession().getAttribute("nickname");
// Null 값 검증
    if (oauthAttributes == null || registrationId == null || email == null) {
      log.error("Required session data is missing. Attributes: {}, RegistrationId: {}, Email: {}", oauthAttributes, registrationId, email);
      return ResponseEntity.badRequest().body(Map.of(
              "message", "Required session data is missing",
              "status", "error"
      ));
    }

    // Null 값을 안전하게 처리 (빈 문자열로 대체)
    Map<String, Object> response = new HashMap<>();
    response.put("registrationId", registrationId);
    response.put("email", email);
    response.put("name", name != null ? name : "");
    response.put("nickname", nickname != null ? nickname : "");
    response.put("oauthAttributes", oauthAttributes);

    log.info("Session Data Retrieved: {}", response);
    return ResponseEntity.ok(response);
  }
}
