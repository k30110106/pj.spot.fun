package com.spot.fun.usr.feed.service.like;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.dto.hashtag.FeedHashtagDTO;
import com.spot.fun.usr.feed.dto.image.FeedImageDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.like.FeedLike;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.repository.hashtag.UserFeedHashtagRepository;
import com.spot.fun.usr.feed.repository.like.UserFeedLikeRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedLikeServiceImpl implements UserFeedLikeService{

  private final UserFeedRepository userFeedRepository;
  private final UserFeedLikeRepository userFeedLikeRepository;
  private final UserRepository userRepository;
  private final UserFeedCommentRepository userFeedCommentRepository;
  private final UserFeedHashtagRepository userFeedHashtagRepository;

  private final UserFeedUtil userFeedUtil;

  @Transactional
  @Override
  public boolean insert(Long idx, Long userIdx) {
    try {
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(idx)
              .orElseThrow(IllegalArgumentException::new);
      User user = userRepository.findByIdx(userIdx)
              .orElseThrow(IllegalArgumentException::new);

      userFeedLikeRepository.save(FeedLike.builder()
                                          .feed(feed)
                                          .user(user)
                                          .build());
      return true;
    } catch (Exception e) {
      log.info("like insert error .. {}", e.getMessage());
      return false;
    }
  }

  @Transactional
  @Override
  public boolean delete(Long idx, Long userIdx) {
    try {
      userFeedLikeRepository.deleteByFeedIdxAndUserIdx(idx, userIdx);
      return true;
    } catch (Exception e) {
      log.info("like delete error .. {}", e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public FeedResponseDTO getLikeListByMypage(FeedRequestDTO feedRequestDTO) {
    int pageSize = feedRequestDTO.getPageSize();
    Long loginUserIdx = feedRequestDTO.getLoginUserDTO().getIdx();

    Long lastIdx = userFeedLikeRepository.findMinIdxByUserIdx(loginUserIdx);

    Pageable pageable = PageRequest.of(0, pageSize, Sort.by("idx").descending());
    Map<String, Object> listMap = getListAndListIdx(userFeedLikeRepository.findFeedsByUserIdxOrderByIdxDesc(feedRequestDTO, pageable));

    Long list_lastIdx = (Long) listMap.get("lastIdx");
    List<FeedLike> list = new ArrayList<>((List<FeedLike>) listMap.get("list"));

    if(!Objects.isNull(lastIdx)
            && !Objects.isNull(list_lastIdx)
            && !Objects.equals(list_lastIdx, lastIdx)) {
      while (true) {
        int re_size = pageSize - list.size();
        if(re_size==0) {
          break;
        }
        feedRequestDTO.setLastId(list_lastIdx);
        pageable = PageRequest.of(0, re_size, Sort.by("idx").descending());
        listMap = getListAndListIdx(userFeedLikeRepository.findFeedsByUserIdxOrderByIdxDesc(feedRequestDTO, pageable));
        list_lastIdx = (Long) listMap.get("lastIdx");

        list.addAll((List<FeedLike>) listMap.get("list"));

        if(Objects.isNull(listMap.get("lastIdx"))
                || Objects.equals(listMap.get("lastIdx"), lastIdx)) {
          break;
        }
      }
    }

    List<FeedDTO> feedDTOS = list.stream()
            .map((like) -> {
              Feed feed = like.getFeed();
              Long likeIdx = like.getIdx();
              Long feedIdx = like.getFeed().getIdx();
              boolean likedYn = true;

              return FeedDTO.builder()
                      .idx(likeIdx)
                      .content(feed.getContent())
                      .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
                      .user(
                              UserDTO.builder()
                                      .idx(feed.getUser().getIdx())
                                      .userId(feed.getUser().getUserId())
                                      //.name(feed.getUser().getName())
                                      .nickname(feed.getUser().getNickname())
                                      .build()
                      )
                      .feedImages(
                              feed.getFeedImages().stream()
                                      .filter((img) -> !img.isDelYn())
                                      .map((img) ->
                                              FeedImageDTO.builder()
                                                      .idx(img.getIdx())
                                                      //.filePath(img.getFilePath())
                                                      .originName(img.getOriginName())
                                                      .uploadName(img.getUploadName())
                                                      //.delYn(img.isDelYn())
                                                      .build()
                                      ).toList()
                      )
                      .likedYn(likedYn)
                      .likeCount(userFeedLikeRepository.countByFeedIdx(feedIdx))
                      .commentCount(userFeedCommentRepository.countByFeedIdxAndDelYnFalseAndParentIdxIsNull(feedIdx))
                      .feedHashtags(
                              userFeedHashtagRepository.findByFeedIdx(feedIdx).stream()
                                      .map((tag) ->
                                              FeedHashtagDTO.builder()
                                                      .idx(tag.getIdx())
                                                      .hashtagIdx(tag.getHashtag().getIdx())
                                                      .tagName(tag.getHashtag().getTagName())
                                                      .build()
                                      ).toList()
                      )
                      .feedIdx(feedIdx)
                      .likeIdx(likeIdx)
                      .build();
            })
            .toList();
    boolean hasNext = ((feedDTOS.size() == feedRequestDTO.getPageSize()) && !ObjectUtils.isEmpty(feedDTOS));

    return FeedResponseDTO.builder()
            .feedDTOS(feedDTOS)
            .hasNext(hasNext)
            .build();
  }

  private Map<String, Object> getListAndListIdx(List<FeedLike> feedLikeList) {
    Map<String, Object> map = new HashMap<>();

    Long lastIdx = feedLikeList.get((feedLikeList.size())-1).getIdx();
    feedLikeList = feedLikeList.stream()
            .filter(filter -> !filter.getFeed().isDelYn())
            .toList();

    map.put("lastIdx", lastIdx);
    map.put("list", feedLikeList);

    return map;
  }
}
