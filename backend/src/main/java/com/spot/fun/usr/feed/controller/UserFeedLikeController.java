package com.spot.fun.usr.feed.controller;

import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.feed.service.like.UserFeedLikeService;
import com.spot.fun.usr.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed/like")
public class UserFeedLikeController {

  private final UserFeedLikeService userFeedLikeService;
  private final AuthTokenUtil authTokenUtil;

  @PostMapping("/{idx}")
  public ResponseEntity<?> insert(HttpServletRequest request, HttpServletResponse response, @PathVariable("idx") Long idx) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    boolean result = userFeedLikeService.insert(idx, loginUserIdx);
    if (!result) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(true);
  }

  @DeleteMapping("/{idx}")
  public ResponseEntity<?> delete(HttpServletRequest request, HttpServletResponse response, @PathVariable("idx") Long idx) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    boolean result = userFeedLikeService.delete(idx, loginUserIdx);
    if (!result) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(true);
  }
}
