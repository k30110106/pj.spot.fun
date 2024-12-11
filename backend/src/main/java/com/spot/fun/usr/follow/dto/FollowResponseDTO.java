package com.spot.fun.usr.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDTO {
  private Long followerCount;
  private Long followingCount;

  private Boolean followStatus;
}
