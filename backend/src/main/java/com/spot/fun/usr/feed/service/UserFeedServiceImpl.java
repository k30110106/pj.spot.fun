package com.spot.fun.usr.feed.service;

import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.dto.hashtag.FeedHashtagDTO;
import com.spot.fun.usr.feed.dto.image.FeedImageDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import com.spot.fun.usr.feed.entity.hashtag.Hashtag;
import com.spot.fun.usr.feed.entity.image.FeedImage;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.repository.hashtag.UserFeedHashtagRepository;
import com.spot.fun.usr.feed.repository.image.UserFeedImageRepository;
import com.spot.fun.usr.feed.repository.like.UserFeedLikeRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
              boolean likedYn = !Objects.isNull(loginUserIdx) && userFeedUtil.isFeedLikedYn(feedIdx, loginUserIdx);

              return FeedDTO.builder()
                      .idx(feedIdx)
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
                                                      //.hashtagIdx(tag.getHashtag().getIdx())
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

    //List<FeedComment> feedComments = userFeedCommentRepository.findByFeedIdxAndDelYnFalse(idx);

    return FeedDTO.builder()
            .idx(feed.getIdx())
            .content(feed.getContent())
            .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
            .user(
                    UserDTO.builder()
                            .idx(feed.getUser().getIdx())
                            .userId(feed.getUser().getUserId())
                            .name(feed.getUser().getName())
                            .nickname(feed.getUser().getNickname())
                            .build()
            )
//                .feedComments(
//                        feedComments.stream()
//                                .map((item) ->
//                                        FeedCommentDTO.builder()
//                                                .idx(item.getIdx())
//                                                .content(item.getContent())
//                                                .regDate(item.getRegDate())
//                                                .build()
//                                ).toList()
//                )
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
                                            //.hashtagIdx(tag.getHashtag().getIdx())
                                            .tagName(tag.getHashtag().getTagName())
                                            .build()
                            ).toList()
            )
//            .feedComments(
//                    userFeedCommentRepository.findByFeedIdxAndDelYnFalse(idx).stream()
//                            .map((comment) ->
//                                    FeedCommentDTO.builder()
//                                            .idx(comment.getIdx())
//                                            .content(comment.getContent())
//                                            .userIdx(comment.getUser().getIdx())
//                                            .regDateStr(userFeedUtil.getDateFormat(comment.getRegDate()))
//                                            .build()
//                            ).toList()
//            )
            .build();
  }

  @Transactional
  @Override
  public Long postInsert(FeedDTO feedDTO) {
    Long userIdx = feedDTO.getUser().getIdx();

    List<FeedImage> feedImages = new ArrayList<>();
    List<MultipartFile> uploadFiles = feedDTO.getUploadFiles();

    if (!ObjectUtils.isEmpty(uploadFiles)) {
      String menuName = "feed"; // 메뉴명 << 폴더구분
      List<Map<String, Object>> savedFiles = fileUploadUtil.saveFiles(uploadFiles, menuName);

      if (uploadFiles.size() != savedFiles.size()) {
        throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
      }

      for (Map<String, Object> file : savedFiles) {
        feedImages.add(
                FeedImage.builder()
                        //.filePath(String.valueOf(file.get("filePath")))
                        .uploadName(String.valueOf(file.get("uploadName")))
                        .originName(String.valueOf(file.get("originName")))
                        .build()
        );
      }
    }

    Feed feed = userFeedRepository.save( // feed table insert
            Feed.builder() // feed entity
                    .content(feedDTO.getContent())
                    .user(User.builder()
                            .idx(userIdx)
                            .build())
                    .build());

    if (!feedImages.isEmpty()) {
      for (FeedImage feedImage : feedImages) {
        feedImage.setFeed(feed);
      }
      userFeedImageRepository.saveAll(feedImages);
    }

    List<FeedHashtag> feedHashtags = new ArrayList<>();
    List<FeedHashtagDTO> feedHashtagDTOS = feedDTO.getFeedHashtags();
    if (!ObjectUtils.isEmpty(feedHashtagDTOS)) {
      for(FeedHashtagDTO dto : feedHashtagDTOS) {
        Hashtag hashtag = userHashtagRepository.findByIdxAndDelYnFalse(dto.getHashtagIdx())
                .orElseThrow(IllegalArgumentException::new);
        feedHashtags.add(
                FeedHashtag.builder()
                        .feed(feed)
                        .hashtag(hashtag)
                        .build()
        );
      }
      userFeedHashtagRepository.saveAll(feedHashtags);
    }

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
              Long loginUserIdx = feedRequestDTO.getLoginUserDTO().getIdx();
              boolean likedYn = !Objects.isNull(loginUserIdx) && userFeedUtil.isFeedLikedYn(feedIdx, loginUserIdx);

              return FeedDTO.builder()
                      .idx(feedIdx)
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
