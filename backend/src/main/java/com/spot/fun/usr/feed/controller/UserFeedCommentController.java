package com.spot.fun.usr.feed.controller;

import com.spot.fun.token.util.AuthTokenUtil;
import com.spot.fun.usr.feed.dto.comment.FeedCommentDTO;
import com.spot.fun.usr.feed.service.comment.UserFeedCommentService;
import com.spot.fun.usr.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed/comment")
public class UserFeedCommentController {

  private final UserFeedCommentService userFeedCommentService;
  private final AuthTokenUtil authTokenUtil;

  @GetMapping("/{idx}")
  public List<FeedCommentDTO> commentList(HttpServletRequest request, HttpServletResponse response, @PathVariable("idx") Long idx) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();

    return userFeedCommentService.getCommentList(idx, loginUserIdx);
  }

  @PostMapping("")
  public ResponseEntity<?> insertComment(HttpServletRequest request, HttpServletResponse response, FeedCommentDTO feedCommentDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
    feedCommentDTO.setUserIdx(loginUserIdx);

    FeedCommentDTO result = userFeedCommentService.insert(feedCommentDTO);
    if (ObjectUtils.isEmpty(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PostMapping("/reply")
  public ResponseEntity<?> insertReply(HttpServletRequest request, HttpServletResponse response, FeedCommentDTO feedCommentDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    feedCommentDTO.setUserIdx(loginUserIdx);

    FeedCommentDTO result = userFeedCommentService.insertReply(feedCommentDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PutMapping("")
  public ResponseEntity<?> updateComment(HttpServletRequest request, HttpServletResponse response, FeedCommentDTO feedCommentDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
    feedCommentDTO.setUserIdx(loginUserIdx);

    FeedCommentDTO result = userFeedCommentService.update(feedCommentDTO);
    if (ObjectUtils.isEmpty(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping("")
  public ResponseEntity<?> deleteComment(HttpServletRequest request, HttpServletResponse response,
                                         FeedCommentDTO feedCommentDTO) {
    UserDTO loginUserDTO = authTokenUtil.validateTokenAndGetUserDTO(request, response);
    Long loginUserIdx = loginUserDTO.getIdx();
    if (Objects.isNull(loginUserIdx)) { // 로그인상태가 아님!!
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
    feedCommentDTO.setUserIdx(loginUserIdx);

    FeedCommentDTO result = userFeedCommentService.delete(feedCommentDTO);
    if (ObjectUtils.isEmpty(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }
}
