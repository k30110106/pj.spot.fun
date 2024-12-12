package com.spot.fun.usr.feed.service;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.dto.hashtag.FeedHashtagDTO;
import com.spot.fun.usr.feed.dto.image.FeedImageDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import com.spot.fun.usr.feed.entity.image.FeedImage;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.repository.hashtag.UserFeedHashtagRepository;
import com.spot.fun.usr.feed.repository.image.UserFeedImageRepository;
import com.spot.fun.usr.feed.repository.like.UserFeedLikeRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import com.spot.fun.usr.user.dto.profile.UserProfileRequestDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.service.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService {
  private final UserFeedRepository userFeedRepository;
  private final UserFeedImageRepository userFeedImageRepository;
  private final UserFeedCommentRepository userFeedCommentRepository;
  private final UserFeedLikeRepository userFeedLikeRepository;
  private final UserFeedHashtagRepository userFeedHashtagRepository;

  private final UserProfileService userProfileService;

  private final UserFeedUtil userFeedUtil;

  @Override
  public FeedResponseDTO getList(FeedRequestDTO feedRequestDTO) {
    // 로그인
    Long loginUserIdx = userFeedUtil.getLoginUserIdx(feedRequestDTO.getLoginUserDTO());


    // 목록 조회
    Pageable pageable = PageRequest.of(0, feedRequestDTO.getPageSize(), Sort.by("idx").descending());
    List<Feed> list = userFeedRepository.findFeedsOrderByIdxDesc(feedRequestDTO, pageable);

    List<FeedDTO> dtos = list.stream()
            .map((feed) -> {
              Long feedIdx = feed.getIdx();
              Long userIdx = feed.getUser().getIdx();
              boolean likedYn = !Objects.isNull(loginUserIdx) && userFeedUtil.isFeedLikedYn(feedIdx, loginUserIdx);

              return FeedDTO.builder()
                      .idx(feedIdx)
                      .content(feed.getContent())
                      .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
                      .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(userIdx).build()))
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
                      ).build();
            }).toList();

    boolean hasNext = ((dtos.size() == feedRequestDTO.getPageSize()) && !ObjectUtils.isEmpty(dtos));

    return FeedResponseDTO.builder()
            .feedDTOS(dtos)
            .hasNext(hasNext)
            .build();
  }

  @Override
  public FeedDTO getDetail(Long idx, Long userIdx) {
    Feed feed = userFeedRepository.findByIdxAndDelYnFalse(idx)
            .orElseThrow(IllegalArgumentException::new);
    boolean likedYn = !Objects.isNull(userIdx) && userFeedUtil.isFeedLikedYn(idx, userIdx);

    return FeedDTO.builder()
            .idx(feed.getIdx())
            .content(feed.getContent())
            .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
            .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(userIdx).build()))
            .feedImages(
                    feed.getFeedImages().stream()
                            .filter((item) -> !item.isDelYn())
                            .map((item) ->
                                    FeedImageDTO.builder()
                                            .idx(item.getIdx())
                                            //.filePath(item.getFilePath())
                                            .uploadName(item.getUploadName())
                                            .originName(item.getOriginName())
                                            .build()
                            ).toList()
            )
            .likedYn(likedYn)
            .likeCount(userFeedLikeRepository.countByFeedIdx(idx))
            .commentCount(userFeedCommentRepository.countByFeedIdxAndDelYnFalseAndParentIdxIsNull(idx))
            .feedHashtags(
                    userFeedHashtagRepository.findByFeedIdx(idx).stream()
                            .map((tag) ->
                                    FeedHashtagDTO.builder()
                                            .idx(tag.getIdx())
                                            .hashtagIdx(tag.getHashtag().getIdx())
                                            .tagName(tag.getHashtag().getTagName())
                                            .build()
                            ).toList()
            )
            .build();
  }

  @Transactional
  @Override
  public Long postInsert(FeedDTO feedDTO) {
    User user = User.builder()
            .idx(feedDTO.getUserIdx())
            .build();

    Feed feed = userFeedRepository.save(
            Feed.builder()
                    .content(feedDTO.getContent())
                    .user(user)
                    .build()
    );

    List<FeedImage> feedImages = userFeedUtil.doUploadFiles(feedDTO.getUploadFiles());
    if (!feedImages.isEmpty()) {
      for (FeedImage feedImage : feedImages) {
        feedImage.setFeed(feed);
      }
      userFeedImageRepository.saveAll(feedImages);
    }
    List<FeedHashtag> feedHashtags = userFeedUtil.doSaveHashtags(feedDTO.getFeedHashtags(), feed);

    return feed.getIdx();
  }

  @Transactional
  @Override
  public FeedDTO delete(FeedDTO feedDTO) {
    try {
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(feedDTO.getIdx())
              .orElseThrow(IllegalArgumentException::new);
      feed.changeDelYn(true); // 삭제

      Feed delete = userFeedRepository.save(feed);

      return FeedDTO.builder()
              .idx(delete.getIdx())
              .delYn(delete.isDelYn())
              .build();
    } catch (Exception e) {
      log.info("comment delete error .. {}", e.getMessage());
      return new FeedDTO();
    }
  }

  @Transactional
  @Override
  public Long postModify(FeedDTO feedDTO) {

    Feed detail = userFeedRepository.findByIdxAndDelYnFalse(feedDTO.getIdx())
            .orElseThrow(IllegalArgumentException::new);
    detail.changeContent(feedDTO.getContent());
    detail.changeUser(User.builder()
            .idx(feedDTO.getUserIdx())
            .build());

    // 삭제 데이터
    List<FeedImage> deleteFiles = userFeedUtil.doDeleteFiles(feedDTO.getDeleteFiles());
    userFeedUtil.doDeleteHashtags(feedDTO.getIdx());

    // 추가 데이터
    Feed feed = userFeedRepository.save(detail);

    List<FeedImage> feedImages = userFeedUtil.doUploadFiles(feedDTO.getUploadFiles());
    if (!feedImages.isEmpty()) {
      for (FeedImage feedImage : feedImages) {
        feedImage.setFeed(feed);
      }
      userFeedImageRepository.saveAll(feedImages);
    }

    List<FeedHashtag> feedHashtags = userFeedUtil.doSaveHashtags(feedDTO.getFeedHashtags(), feed);

    return feed.getIdx();
  }

  @Override
  public FeedResponseDTO getFeedListByMypage(FeedRequestDTO feedRequestDTO) {
    Long feedCount = userFeedRepository.countByUserIdxAndDelYnFalse(feedRequestDTO.getUserIdx());


    // 목록 조회
    Pageable pageable = PageRequest.of(0, feedRequestDTO.getPageSize(), Sort.by("idx").descending());
    List<FeedDTO> list = userFeedRepository.findFeedsByUserIdxOrderByIdxDesc(feedRequestDTO, pageable).stream()
            .map((feed) -> {
              Long feedIdx = feed.getIdx();
              Long userIdx = feed.getUser().getIdx();
              Long loginUserIdx = feedRequestDTO.getLoginUserDTO().getIdx();
              boolean likedYn = !Objects.isNull(loginUserIdx) && userFeedUtil.isFeedLikedYn(feedIdx, loginUserIdx);

              return FeedDTO.builder()
                      .idx(feedIdx)
                      .content(feed.getContent())
                      .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
                      .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(userIdx).build()))
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
                      ).build();
            })
            .toList();

    boolean hasNext = ((list.size() == feedRequestDTO.getPageSize()) && !ObjectUtils.isEmpty(list));

    return FeedResponseDTO.builder()
            .feedDTOS(list)
            .feedCount(feedCount)
            .hasNext(hasNext)
            .build();
  }
}
