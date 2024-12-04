package com.spot.fun.usr.feed.repository.image;

import com.spot.fun.usr.feed.entity.image.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedImageRepository extends JpaRepository<FeedImage, Long> {

}
