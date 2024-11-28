package com.spot.fun.usr.feed.controller;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

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
  public FeedDTO detail(@PathVariable("idx") Long idx) {
    return userFeedService.getDetail(idx);
  }

  @PostMapping("")
  public Long insert(FeedDTO feedDTO) {
    return userFeedService.postInsert(feedDTO);
  }

}
