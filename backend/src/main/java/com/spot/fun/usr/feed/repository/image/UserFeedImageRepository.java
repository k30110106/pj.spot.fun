package com.spot.fun.usr.feed.repository.image;

import com.spot.fun.usr.feed.entity.image.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFeedImageRepository extends JpaRepository<FeedImage, Long> {
  Optional<FeedImage> findByIdxAndDelYnFalse(Long idx);
}
