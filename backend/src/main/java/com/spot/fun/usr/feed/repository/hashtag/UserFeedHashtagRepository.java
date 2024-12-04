package com.spot.fun.usr.feed.repository.hashtag;

import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFeedHashtagRepository extends JpaRepository<FeedHashtag, Long> {
    List<FeedHashtag> findByFeedIdx(long feedIdx);
}
