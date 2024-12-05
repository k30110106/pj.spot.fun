package com.spot.fun.usr.feed.repository.comment;

import com.spot.fun.usr.feed.entity.comment.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFeedCommentRepository extends JpaRepository<FeedComment, Long> {
  List<FeedComment> findByFeedIdxAndDelYnFalseAndParentIdxIsNull(Long feedIdx);
  List<FeedComment> findByParentIdxAndDelYnFalse(Long feedIdx);
  Long countByFeedIdxAndDelYnFalseAndParentIdxIsNull(Long feedIdx);
  Optional<FeedComment> findByIdxAndDelYnFalse(Long idx);

}
