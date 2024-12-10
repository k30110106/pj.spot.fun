package com.spot.fun.usr.feed.repository.like;

import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.entity.like.FeedLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFeedLikeRepository extends JpaRepository<FeedLike, Long> {
  Long countByFeedIdx(Long feedIdx);

  boolean existsByFeedIdxAndUserIdx(Long feedIdx, Long userIdx);

  void deleteByFeedIdxAndUserIdx(Long feedIdx, Long userIdx);

  @Query("SELECT f FROM FeedLike f " +
          "INNER JOIN f.feed feed " +
          "WHERE f.user.idx = :#{#feed.userIdx} " +
          "AND (:#{#feed.lastId}=0 or f.idx < :#{#feed.lastId})")
  List<FeedLike> findFeedsByUserIdxOrderByIdxDesc(@Param("feed") FeedRequestDTO feedRequestDTO, Pageable pageable);


}
