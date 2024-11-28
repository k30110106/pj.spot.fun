package com.spot.fun.usr.feed.service;

import com.spot.fun.file.FileUploadUtil;
import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedImageDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.FeedImage;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.user.dto.UserDTO;
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
  private final FileUploadUtil fileUploadUtil;

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
                                                                              .filePath(img.getFilePath())
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

  @Transactional
  @Override
  public Long postInsert(FeedDTO feedDTO) {
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
                      .filePath(String.valueOf(file.get("filePath")))
                      .uploadName(String.valueOf(file.get("uploadName")))
                      .originName(String.valueOf(file.get("originName")))
                      .build()
        );
      }
    }

    Feed feed = userFeedRepository.save(Feed.builder()
                                  .content(feedDTO.getContent())
                                  .feedImages(feedImages)
                                  .build());

    return feed.getIdx();
  }
}
