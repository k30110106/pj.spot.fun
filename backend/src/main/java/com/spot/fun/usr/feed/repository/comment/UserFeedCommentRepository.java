package com.spot.fun.usr.feed.repository.comment;

import com.spot.fun.usr.feed.entity.comment.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFeedCommentRepository extends JpaRepository<FeedComment, Long> {
  List<FeedComment> findByFeedIdxAndDelYnFalse(Long feedIdx);
  Long countByFeedIdxAndDelYnFalse(Long feedIdx);
}
