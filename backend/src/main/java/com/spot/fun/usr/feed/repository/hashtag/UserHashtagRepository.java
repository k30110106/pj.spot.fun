package com.spot.fun.usr.feed.repository.hashtag;

import com.spot.fun.usr.feed.entity.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHashtagRepository extends JpaRepository<Hashtag, Long> {
  Optional<Hashtag> findByIdxAndDelYnFalse(Long idx);

  List<Hashtag> findByDelYnFalse();
}
