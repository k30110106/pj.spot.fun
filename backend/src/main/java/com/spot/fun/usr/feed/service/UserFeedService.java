package com.spot.fun.usr.feed.service;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;

import java.util.List;

public interface UserFeedService {
  FeedResponseDTO getList(FeedRequestDTO feedRequestDTO);

  FeedDTO getDetail(Long idx, Long userIdx);

  Long postInsert(FeedDTO feedDTO);

  FeedDTO delete(FeedDTO feedDTO);

  Long postModify(FeedDTO feedDTO);

  FeedResponseDTO getListByMypage(FeedRequestDTO feedRequestDTO);
}
