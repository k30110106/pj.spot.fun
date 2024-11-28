package com.spot.fun.usr.feed.controller;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed")
public class UserFeedController {
  private final UserFeedService userFeedService;

  @GetMapping("")
  public FeedResponseDTO list(FeedRequestDTO feedRequestDTO) {
    return userFeedService.getList(feedRequestDTO);
  }

  @GetMapping("/{idx}")
  public FeedDTO detail(@PathVariable int idx) {
    return null;
  }

  @PostMapping("")
  public Long insert(FeedDTO feedDTO) {
    return userFeedService.postInsert(feedDTO);
  }

  @GetMapping("/test")
  public Map<String, Object> list2() {
    return Map.of("123", "asd");
//    return userFeedService.getList(feedRequestDTO);
  }
}
