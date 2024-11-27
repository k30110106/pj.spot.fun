package com.spot.fun.usr.signup.controller;


import com.spot.fun.usr.signup.service.SignupService;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usr/signup")
@RequiredArgsConstructor
public class SignupController {
  private final SignupService signupService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserDTO userDTO){
    try {
      // 서비스 호출: 데이터베이스에 저장
      signupService.signup(userDTO);
      return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입에 성공했습니다!"));
    } catch (IllegalArgumentException e) {
      // 유효성 검사 실패
      return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
    } catch (Exception e) {
      // 기타 서버 에러
      return ResponseEntity.status(500).body(Map.of("status", "error", "message", "서버에 문제가 발생했습니다."));
    }
  }
}
