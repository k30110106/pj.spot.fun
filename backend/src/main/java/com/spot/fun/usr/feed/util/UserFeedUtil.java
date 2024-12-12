package com.spot.fun.usr.feed.util;

import com.spot.fun.file.FileUploadUtil;
import com.spot.fun.usr.feed.dto.hashtag.FeedHashtagDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import com.spot.fun.usr.feed.entity.hashtag.Hashtag;
import com.spot.fun.usr.feed.entity.image.FeedImage;
import com.spot.fun.usr.feed.repository.hashtag.UserFeedHashtagRepository;
import com.spot.fun.usr.feed.repository.hashtag.UserHashtagRepository;
import com.spot.fun.usr.feed.repository.image.UserFeedImageRepository;
import com.spot.fun.usr.feed.repository.like.UserFeedLikeRepository;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserFeedUtil {
  private final UserFeedLikeRepository userFeedLikeRepository;
  private final UserHashtagRepository userHashtagRepository;
  private final UserFeedHashtagRepository userFeedHashtagRepository;
  private final UserFeedImageRepository userFeedImageRepository;
  private final FileUploadUtil fileUploadUtil;

  public boolean isFeedLikedYn(Long feedIdx, Long userIdx) {
    boolean likedYn = false;
    if (!Objects.isNull(userIdx)) {
      likedYn = userFeedLikeRepository.existsByFeedIdxAndUserIdx(feedIdx, userIdx);
    }
    return likedYn;
  }

  public boolean isFeedCommentLikedYn(Long commentIdx, Long userIdx) {
    boolean likedYn = false;
    if (!Objects.isNull(userIdx)) {
      // 댓글 좋아요기능 사용여부 확인필요 >>> 미사용
      //likedYn = userFeedLikeRepository.existsByFeedIdxAndUserIdx(commentIdx, userIdx);
    }
    return likedYn;
  }

  public String getDateFormat(LocalDateTime localDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");
    return localDateTime.format(formatter);
  }

  public Long getLoginUserIdx(UserDTO userDTO) {
    return Optional.ofNullable(userDTO)
            .map(UserDTO::getIdx)
            .orElse(null);
  }

  public List<FeedImage> doUploadFiles(List<MultipartFile> uploadFiles) {
    List<FeedImage> feedImages = new ArrayList<>();

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
    return feedImages;
  }

  public List<FeedImage> doDeleteFiles(List<Long> deleteFiles) {
    List<FeedImage> feedImages = new ArrayList<>();
    if (!ObjectUtils.isEmpty(deleteFiles)) {
      // 실제로 파일을 삭제하지는 않고.. 삭제여부만 변경처리
      for (Long idx : deleteFiles) {
        FeedImage image = userFeedImageRepository.findByIdxAndDelYnFalse(idx)
                .orElseThrow(IllegalArgumentException::new);
        image.changeDelYn(true);
        feedImages.add(image);
      }
      userFeedImageRepository.saveAll(feedImages);
    }
    return feedImages;
  }

  public List<FeedHashtag> doSaveHashtags(List<FeedHashtagDTO> hashtags, Feed feed) {
    List<FeedHashtag> feedHashtags = new ArrayList<>();
    if (!ObjectUtils.isEmpty(hashtags)) {
      for (FeedHashtagDTO dto : hashtags) {
        Hashtag hashtag = userHashtagRepository.findByIdxAndDelYnFalse(dto.getHashtagIdx())
                .orElseThrow(IllegalArgumentException::new);
        feedHashtags.add(
                FeedHashtag.builder()
                        .feed(feed)
                        .hashtag(hashtag)
                        .build());
      }
      userFeedHashtagRepository.saveAll(feedHashtags);
    }
    return feedHashtags;
  }

  public void doDeleteHashtags(Long feedIdx) {
    // 피드 idx 기반으로 데이터 삭제
    if (!Objects.isNull(feedIdx)) {
      userFeedHashtagRepository.deleteByFeedIdx(feedIdx);
    }
  }


}
