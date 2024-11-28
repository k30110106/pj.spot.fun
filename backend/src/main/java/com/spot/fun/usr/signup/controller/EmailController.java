package com.spot.fun.usr.signup.controller;

import com.spot.fun.usr.signup.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usr/email")
@RequiredArgsConstructor
public class EmailController {
  private final EmailService emailService;

  @PostMapping("/send-verification")
  public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      return ResponseEntity.badRequest().body(Map.of("message", "유효한 이메일 주소가 아닙니다."));
    }

    String verificationCode = emailService.sendVerificationEmail(email);
    return ResponseEntity.ok(Map.of("verificationCode", verificationCode)); // 인증 코드 반환
  }
}