package com.spot.fun.usr.user.repository.profile;

import com.spot.fun.usr.user.entity.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
  Optional<UserProfile> findByUserIdx(Long userIdx);
}
