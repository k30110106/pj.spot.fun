package com.spot.fun.usr.login.controller;

import com.spot.fun.token.dto.AuthTokenDTO;
import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.login.service.UserLoginService;
import com.spot.fun.usr.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/login")
public class UserLoginController {
  private final UserLoginService userLoginService;
  private final AuthTokenUtil authTokenUtil;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
    try {
      // 자체 로그인 요청 처리
      if ("LOCAL".equalsIgnoreCase(userDTO.getProvider())) {
        log.info("LOCAL login request received: userId={}, provider={}", userDTO.getUserId(), userDTO.getProvider());
      }

      AuthTokenDTO authTokenDTO = userLoginService.doLogin(userDTO);

      log.info("Generated AuthTokenDTO: accessToken={}, refreshToken={}, nickname={}",
              authTokenDTO.getAccessToken(),
              authTokenDTO.getRefreshToken(),
              authTokenDTO.getNickname());

      // 쿠키 생성
      authTokenUtil.makeAccessToken(response, authTokenDTO.getAccessToken());
      authTokenUtil.makeRefreshToken(response, authTokenDTO.getRefreshToken());

      return ResponseEntity.status(HttpStatus.OK).body(authTokenDTO.getNickname());
    } catch (AuthenticationException e) {
      log.error("Authentication failed: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    } catch (Exception e) {
      log.error("Login Controller Error: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
  }

  @PostMapping("/token/check")
  public ResponseEntity<?> tokenCheck(HttpServletRequest request, HttpServletResponse response) {
    try {
      UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);

//      Long userIdx = Optional.ofNullable(loginUserDTO)
//              .map(UserDTO::getIdx)
//              .orElse(null);
      if (loginUserDTO == null || loginUserDTO.getIdx() == null) {
        log.warn("Invalid or missing token.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
      }

      // userIdx와 nickname을 포함한 Map 반환
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("userIdx", loginUserDTO.getIdx());
      responseBody.put("nickname", loginUserDTO.getNickname());

      return ResponseEntity.ok(responseBody);
    } catch (Exception e) {
      log.error("tokenCheck Error: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
