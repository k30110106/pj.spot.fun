package com.spot.fun.usr.feed.controller;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/feed/{idx}")
  public FeedDTO detail(@PathVariable int idx) {
    return null;
  }
}
