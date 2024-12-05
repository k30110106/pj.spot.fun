package com.spot.fun.usr.feed.repository.comment;

import com.spot.fun.usr.feed.entity.comment.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFeedCommentRepository extends JpaRepository<FeedComment, Long> {
  List<FeedComment> findByFeedIdxAndDelYnFalse(Long feedIdx);
  Long countByFeedIdxAndDelYnFalse(Long feedIdx);
  Optional<FeedComment> findByIdxAndDelYnFalse(Long idx);
  List<FeedComment> findByParentIdxAndDelYnFalse(Long parentIdx);

  @Query("SELECT c FROM FeedComment c LEFT JOIN FETCH c.replyList WHERE c.feed.idx = :feedIdx")
  List<FeedComment> findFeedCommentWithReplyList(@Param("feedIdx") Long feedIdx);
}
