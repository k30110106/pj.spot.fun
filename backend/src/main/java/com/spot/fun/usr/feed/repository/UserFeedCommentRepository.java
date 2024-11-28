package com.spot.fun.usr.feed.repository;

import com.spot.fun.usr.feed.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFeedCommentRepository extends JpaRepository<FeedComment, Long> {
  List<FeedComment> findByFeedIdxAndDelYnFalse(Long feedIdx);
}
