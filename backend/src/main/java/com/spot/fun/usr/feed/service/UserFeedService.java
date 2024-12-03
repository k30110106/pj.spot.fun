package com.spot.fun.usr.feed.service;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;

public interface UserFeedService {
  FeedResponseDTO getList(FeedRequestDTO feedRequestDTO);
  FeedDTO getDetail(Long idx);
  Long postInsert(FeedDTO feedDTO);
}
