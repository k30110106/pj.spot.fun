package com.spot.fun.usr.feed.repository.hashtag;

import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFeedHashtagRepository extends JpaRepository<FeedHashtag, Long> {
  List<FeedHashtag> findByFeedIdx(Long feedIdx);

  void deleteByFeedIdx(Long feedIdx);
}
