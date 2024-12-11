package com.spot.fun.usr.follow.repository;

import com.spot.fun.usr.follow.entity.Follow;
import com.spot.fun.usr.follow.entity.composite.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<Follow, FollowKey> {

  Long countByFollowerIdx(Long followerIdx);

  Long countByFollowingIdx(Long followingIdx);

  boolean existsByKey(FollowKey followKey);

  void deleteByKey(FollowKey followKey);
}
