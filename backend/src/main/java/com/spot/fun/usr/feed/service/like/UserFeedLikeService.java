package com.spot.fun.usr.feed.service.like;

import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;

public interface UserFeedLikeService {
  boolean insert(Long idx, Long userIdx);

  boolean delete(Long idx, Long userIdx);

  FeedResponseDTO getLikeListByMypage(FeedRequestDTO feedRequestDTO);
}
