package com.spot.fun.usr.follow.service;

import com.spot.fun.usr.follow.dto.FollowRequestDTO;
import com.spot.fun.usr.follow.dto.FollowResponseDTO;
import com.spot.fun.usr.follow.entity.Follow;
import com.spot.fun.usr.follow.entity.composite.FollowKey;
import com.spot.fun.usr.follow.repository.UserFollowRepository;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {

  private final UserFollowRepository userFollowRepository;
  private final UserRepository userRepository;

  @Override
  public FollowResponseDTO getCountAll(FollowRequestDTO followRequestDTO) {
    Long userIdx = followRequestDTO.getUserIdx();
    Long followerCount = userFollowRepository.countByFollowerIdx(userIdx);
    Long followingCount = userFollowRepository.countByFollowingIdx(userIdx);

    return FollowResponseDTO.builder()
            .followerCount(followerCount)
            .followingCount(followingCount)
            .build();
  }

  @Override
  public FollowResponseDTO getStatus(FollowRequestDTO followRequestDTO) {
    FollowKey followKey = FollowKey.builder()
            .followerIdx(followRequestDTO.getFollowerIdx())
            .followingIdx(followRequestDTO.getFollowingIdx())
            .build();

    boolean status = userFollowRepository.existsByKey(followKey);

    return FollowResponseDTO.builder()
            .followStatus(status)
            .build();
  }

  @Transactional
  @Override
  public FollowResponseDTO postStatus(FollowRequestDTO followRequestDTO) {
    User follower = userRepository.findByIdx(followRequestDTO.getFollowerIdx())
            .orElseThrow(IllegalArgumentException::new);
    User following = userRepository.findByIdx(followRequestDTO.getFollowingIdx())
            .orElseThrow(IllegalArgumentException::new);

    Follow follow = userFollowRepository.save(Follow.builder()
            .follower(follower)
            .following(following)
            .build());

    return FollowResponseDTO.builder()
            .followStatus(true)
            .build();
  }

  @Transactional
  @Override
  public FollowResponseDTO deleteStatus(FollowRequestDTO followRequestDTO) {
    FollowKey followKey = FollowKey.builder()
            .followerIdx(followRequestDTO.getFollowerIdx())
            .followingIdx(followRequestDTO.getFollowingIdx())
            .build();

    userFollowRepository.deleteByKey(followKey);

    return FollowResponseDTO.builder()
            .followStatus(false)
            .build();
  }
}
