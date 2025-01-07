package com.spot.fun.usr.spot.repository;

import com.spot.fun.usr.spot.entity.SpotUserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotUserMapRepository extends JpaRepository<SpotUserMap, Long> {
  @Query("SELECT map.spotId FROM SpotUserMap map WHERE map.userIdx=:userIdx")
  List<Long> findSpotIdsByUserIdx(@Param("userIdx") Long userIdx);

  boolean existsSpotByUserIdxAndSpotId(Long userIdx, Long spotIdx);
}