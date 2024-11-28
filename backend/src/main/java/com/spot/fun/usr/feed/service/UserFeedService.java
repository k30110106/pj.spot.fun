package com.spot.fun.usr.feed.service;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.entity.Feed;

public interface UserFeedService {
  FeedResponseDTO getList(FeedRequestDTO feedRequestDTO);
  void postInsert(FeedDTO feedDTO);
}
