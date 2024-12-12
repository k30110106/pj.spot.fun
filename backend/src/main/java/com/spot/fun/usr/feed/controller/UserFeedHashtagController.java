package com.spot.fun.usr.feed.controller;

import com.spot.fun.usr.feed.dto.hashtag.HashtagDTO;
import com.spot.fun.usr.feed.service.hashtag.UserFeedHashtagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed/hashtag")
public class UserFeedHashtagController {

  private final UserFeedHashtagService userFeedHashtagService;

  @GetMapping("")
  public List<HashtagDTO> list() {
    return userFeedHashtagService.getList();
  }
}
