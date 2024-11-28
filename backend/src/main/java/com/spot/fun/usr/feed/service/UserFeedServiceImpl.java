package com.spot.fun.usr.feed.service;

import com.spot.fun.config.jwt.JwtTokenProvider;
import com.spot.fun.config.jwt.JwtTokenUtil;
import com.spot.fun.file.FileUploadUtil;
import com.spot.fun.usr.feed.dto.*;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.FeedComment;
import com.spot.fun.usr.feed.entity.FeedImage;
import com.spot.fun.usr.feed.repository.UserFeedCommentRepository;
import com.spot.fun.usr.feed.repository.UserFeedImageRepository;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService {
  private final UserFeedRepository userFeedRepository;
  private final UserFeedImageRepository userFeedImageRepository;
  private final UserFeedCommentRepository userFeedCommentRepository;
  private final FileUploadUtil fileUploadUtil;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public FeedResponseDTO getList(FeedRequestDTO feedRequestDTO) {
    Pageable pageable = PageRequest.of(0, feedRequestDTO.getPageSize(), Sort.by("idx").descending());
    List<Feed> list = userFeedRepository.findFeedsOrderByIdxDesc(feedRequestDTO, pageable);
    List<FeedDTO> dtos = list.stream().map((feed) -> {
                            return FeedDTO.builder()
                                    .idx(feed.getIdx())
                                    .content(feed.getContent())
                                    .regDate(feed.getRegDate())
                                    .userDTO(
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
                                                    .map((img) -> FeedImageDTO.builder()
                                                                            //.idx(img.getIdx())
                                                                            //.filePath(img.getFilePath())
                                                                            .originName(img.getOriginName())
                                                                            .uploadName(img.getUploadName())
                                                                            //.delYn(img.isDelYn())
                                                                            .build()
                                                    ).toList()
                                    )
                                    .build();
                          }).toList();
    boolean hasNext = (dtos.size()==feedRequestDTO.getPageSize());

    return FeedResponseDTO.builder()
                        .feedDTOS(dtos)
                        .hasNext(hasNext)
                        .build();
  }

  @Override
  public FeedDTO getDetail(Long idx) {
    Feed feed = userFeedRepository.findByIdxAndDelYnFalse(idx)
            .orElseThrow(IllegalArgumentException::new);

    List<FeedComment> feedComments = userFeedCommentRepository.findByFeedIdxAndDelYnFalse(idx);

    return FeedDTO.builder()
            .idx(feed.getIdx())
            .content(feed.getContent())
            .regDate(feed.getRegDate())
            .userDTO(
              UserDTO.builder()
                    .idx(feed.getUser().getIdx())
                    .userId(feed.getUser().getUserId())
                    .name(feed.getUser().getName())
                    .nickname(feed.getUser().getNickname())
                    .build())
            .feedComments(
              feedComments.stream().map((item) -> {
                 return FeedCommentDTO.builder()
                                     .idx(item.getIdx())
                                     .content(item.getContent())
                                     .regDate(item.getRegDate())
                                     .build();
              }).toList())
            .feedImages(
              feed.getFeedImages().stream()
                      .filter((item) -> !item.isDelYn())
                      .map((item) -> {
                        return FeedImageDTO.builder()
                                          //.filePath(item.getFilePath())
                                          .uploadName(item.getUploadName())
                                          .originName(item.getOriginName())
                                          .build();
              }).toList())
            .build();
  }

  @Transactional
  @Override
  public Long postInsert(FeedDTO feedDTO) {
//    String accessToken = JwtTokenUtil.getJwtToken();
//    Long userIdx = jwtTokenProvider.getUserIdx(accessToken);
    Long userIdx = 1L;

    List<FeedImage> feedImages = new ArrayList<>();
    List<MultipartFile> uploadFiles = feedDTO.getUploadFiles();

    if(!ObjectUtils.isEmpty(uploadFiles)) {
      String menuName = "feed"; // 메뉴명 << 폴더구분
      List<Map<String, Object>> savedFiles = fileUploadUtil.saveFiles(uploadFiles, menuName);

      if(uploadFiles.size() != savedFiles.size()) {
        throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
      }

      for(Map<String, Object> file : savedFiles) {
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

    if(!feedImages.isEmpty()) {
      for(FeedImage feedImage : feedImages) {
        feedImage.setFeed(feed);
      }
      userFeedImageRepository.saveAll(feedImages);
    }

    return feed.getIdx();
  }
}
