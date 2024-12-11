package com.spot.fun.usr.mypage.controller;

import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import com.spot.fun.usr.feed.service.like.UserFeedLikeService;
import com.spot.fun.usr.mypage.service.UserMypageService;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/mypage")
public class UserMypageController {
  private final UserMypageService userMypageService;
  private final UserFeedService userFeedService;
  private final UserFeedLikeService userFeedLikeService;
  private final UserService userService;

  private final AuthTokenUtil authTokenUtil;

  @GetMapping("/info")
  public UserDTO info() {
    return userMypageService.findByIdx();
  }

  @GetMapping("/exists")
  public ResponseEntity<?> existsUser(UserDTO userDTO) {
    UserDTO info = userService.findByIdx(userDTO.getIdx());
    return ResponseEntity.status(HttpStatus.OK).body(!Objects.isNull(info));
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUser(UserDTO userDTO) {
    UserDTO info = userMypageService.getUser(userDTO);
    return ResponseEntity.status(HttpStatus.OK).body(info);
  }

  @GetMapping("/feed")
  public ResponseEntity<?> feedList(HttpServletRequest request, HttpServletResponse response,
                                    FeedRequestDTO feedRequestDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    if (!Objects.isNull(loginUserDTO.getIdx())) {
      feedRequestDTO.setLoginUserDTO(loginUserDTO);
    } else { // 비로그인 상태는 접근불가
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    FeedResponseDTO list = userFeedService.getFeedListByMypage(feedRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }

  @GetMapping("/like")
  public ResponseEntity<?> likeList(HttpServletRequest request, HttpServletResponse response,
                                    FeedRequestDTO feedRequestDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    if (!Objects.isNull(loginUserDTO.getIdx())) {
      feedRequestDTO.setLoginUserDTO(loginUserDTO);
    } else { // 비로그인 상태는 접근불가
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    FeedResponseDTO list = userFeedLikeService.getLikeListByMypage(feedRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }
}
