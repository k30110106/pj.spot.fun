package com.spot.fun.usr.signup.controller;


import com.spot.fun.usr.signup.service.SignupService;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/usr/signup")
@RequiredArgsConstructor
public class SignupController {
  private final SignupService signupService;
  private final UserRepository userRepository;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserDTO userDTO){
    try {
      // 서비스 호출: 데이터베이스에 저장
      signupService.signup(userDTO);
      return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입에 성공했습니다!"));
    } catch (IllegalArgumentException e) {
      // 유효성 검사 실패 - 실패한 필드 정보와 메시지를 반환
      String[] errorInfo = e.getMessage().split(":");
      String field = errorInfo.length > 1 ? errorInfo[0] : "unknown";
      String message = errorInfo.length > 1 ? errorInfo[1] : e.getMessage();

      return ResponseEntity.badRequest().body(Map.of("status", "error", "field", field, "message", message));
    } catch (Exception e) {
      // 기타 서버 에러
      return ResponseEntity.status(500).body(Map.of("status", "error", "message", "서버에 문제가 발생했습니다."));
    }
  }

  @PostMapping("/check-duplicate")
  public ResponseEntity<?> checkDuplicate(@RequestBody Map<String, String> request) {
    String field = request.get("field");
    String value = request.get("value");

    boolean isDuplicate = false;

    if ("userId".equals(field)) {
      isDuplicate = userRepository.existsByUserId(value);
    } else if ("nickname".equals(field)) {
      isDuplicate = userRepository.existsByNickname(value);
    }

    return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
  }

  @PostMapping("/social-signup")
  public ResponseEntity<?> socialSignup(@RequestBody UserDTO userDTO, HttpServletRequest request) {
    try {
      // 세션에서 provider 값 가져오기
      String provider = (String) request.getSession().getAttribute("registrationId");
      userDTO.setProvider(provider); // UserDTO에 provider 설정

      signupService.socialSignup(userDTO);

      return ResponseEntity.ok(Map.of("status", "success", "message", "소셜 회원가입 성공!"));
    } catch (IllegalArgumentException e) {
      String[] errorInfo = e.getMessage().split(":");
      String field = errorInfo.length > 1 ? errorInfo[0] : "unknown";
      String message = errorInfo.length > 1 ? errorInfo[1] : e.getMessage();

      return ResponseEntity.badRequest().body(Map.of("status", "error", "field", field, "message", message));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of("status", "error", "message", "서버에 문제가 발생했습니다."));
    }
  }



}
