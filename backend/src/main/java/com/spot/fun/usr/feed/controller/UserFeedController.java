package com.spot.fun.usr.feed.controller;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed")
public class UserFeedController {
  private final UserFeedService userFeedService;

  @GetMapping("")
  public List<FeedDTO> list(FeedDTO feedDTO) {
    List<FeedDTO> list = userFeedService.getList();
    return list;
  }

  @GetMapping("/feed/{idx}")
  public FeedDTO detail(@PathVariable int idx) {

    return null;
  }

  @PostMapping("")
  public ResponseEntity<?> insert(FeedDTO feedDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
