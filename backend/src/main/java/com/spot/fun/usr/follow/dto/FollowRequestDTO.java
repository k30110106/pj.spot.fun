package com.spot.fun.usr.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequestDTO {
  private Long followerIdx;
  private Long followingIdx;

  private Long userIdx; // login userIdx
}
