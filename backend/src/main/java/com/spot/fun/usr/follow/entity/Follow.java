package com.spot.fun.usr.follow.entity;

import com.spot.fun.usr.follow.entity.composite.FollowKey;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_follow")
@NoArgsConstructor
@Getter
@Entity
public class Follow {

  @EmbeddedId
  private FollowKey key;  // 복합키

  @ManyToOne
  @MapsId("followerIdx")
  @JoinColumn(name = "follower_idx")
  private User follower; // 사용자

  @ManyToOne
  @MapsId("followingIdx")
  @JoinColumn(name = "following_idx")
  private User following; // 팔로우 대상

  @Builder
  public Follow(User follower, User following) {
    this.follower = follower;
    this.following = following;
    this.key = new FollowKey(follower.getIdx(), following.getIdx());
  }
}
