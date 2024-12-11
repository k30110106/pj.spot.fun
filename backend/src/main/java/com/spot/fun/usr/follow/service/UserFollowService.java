package com.spot.fun.usr.follow.service;

import com.spot.fun.usr.follow.dto.FollowRequestDTO;
import com.spot.fun.usr.follow.dto.FollowResponseDTO;

public interface UserFollowService {
  FollowResponseDTO getCountAll(FollowRequestDTO followRequestDTO);

  FollowResponseDTO getStatus(FollowRequestDTO followRequestDTO);

  FollowResponseDTO postStatus(FollowRequestDTO followRequestDTO);

  FollowResponseDTO deleteStatus(FollowRequestDTO followRequestDTO);
}
