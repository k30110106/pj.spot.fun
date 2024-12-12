package com.spot.fun.usr.feed.service.hashtag;

import com.spot.fun.usr.feed.dto.hashtag.HashtagDTO;
import com.spot.fun.usr.feed.entity.hashtag.Hashtag;
import com.spot.fun.usr.feed.repository.hashtag.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedHashtagServiceImpl implements UserFeedHashtagService {

  private final UserHashtagRepository userHashtagRepository;

  @Override
  public List<HashtagDTO> getList() {
    List<Hashtag> list = userHashtagRepository.findByDelYnFalse();

    return list.stream().map((tag) ->
            HashtagDTO.builder()
                    .idx(tag.getIdx())
                    .tagName(tag.getTagName())
                    .build()).toList();
  }
}
