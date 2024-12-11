package com.spot.fun.usr.follow.entity.composite;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class FollowKey implements Serializable {

  private Long followerIdx;
  private Long followingIdx;

  @Builder
  public FollowKey(Long followerIdx, Long followingIdx) {
    this.followerIdx = followerIdx;
    this.followingIdx = followingIdx;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FollowKey followKey = (FollowKey) o;
    return Objects.equals(followerIdx, followKey.followerIdx) &&
            Objects.equals(followingIdx, followKey.followingIdx);
  }

  @Override
  public int hashCode() {
    return Objects.hash(followerIdx, followingIdx);
  }
}
