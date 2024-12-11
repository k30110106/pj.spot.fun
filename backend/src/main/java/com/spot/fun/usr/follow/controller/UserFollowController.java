package com.spot.fun.usr.follow.controller;

import com.spot.fun.usr.follow.dto.FollowRequestDTO;
import com.spot.fun.usr.follow.dto.FollowResponseDTO;
import com.spot.fun.usr.follow.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/usr/follow")
public class UserFollowController {

  private final UserFollowService userFollowService;

  @GetMapping("/count/all")
  public ResponseEntity<?> getCountAll(FollowRequestDTO followRequestDTO) {
    FollowResponseDTO followResponseDTO = userFollowService.getCountAll(followRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(followResponseDTO);
  }

  @GetMapping("/status")
  public ResponseEntity<?> getStatus(FollowRequestDTO followRequestDTO) {
    FollowResponseDTO followResponseDTO = userFollowService.getStatus(followRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(followResponseDTO);
  }

  @PostMapping("")
  public ResponseEntity<?> postStatus(FollowRequestDTO followRequestDTO) {
    FollowResponseDTO followResponseDTO = userFollowService.postStatus(followRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(followResponseDTO);
  }

  @DeleteMapping("")
  public ResponseEntity<?> deleteStatus(FollowRequestDTO followRequestDTO) {
    FollowResponseDTO followResponseDTO = userFollowService.deleteStatus(followRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(followResponseDTO);
  }
}
