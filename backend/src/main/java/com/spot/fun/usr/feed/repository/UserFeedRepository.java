package com.spot.fun.usr.feed.repository;

import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFeedRepository extends JpaRepository<Feed, Long> {

    @Query("SELECT f FROM Feed f WHERE f.delYn = false " +
            "AND (:#{#feedRequestDTO.lastId}=0 or f.idx < :#{#feedRequestDTO.lastId}) " +
            "AND (:#{#feedRequestDTO.searchValue} is NULL or :#{#feedRequestDTO.searchValue}='' or f.content LIKE %:#{#feedRequestDTO.searchValue}%)"
    )
    List<Feed> findFeedsOrderByIdxDesc(FeedRequestDTO feedRequestDTO, Pageable pageable);
}
