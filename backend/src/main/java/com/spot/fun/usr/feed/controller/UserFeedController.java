package com.spot.fun.usr.feed.controller;

import com.spot.fun.file.FileUploadUtil;
import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.FeedRequestDTO;
import com.spot.fun.usr.feed.dto.FeedResponseDTO;
import com.spot.fun.usr.feed.service.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usr/feed")
public class UserFeedController {
  private final UserFeedService userFeedService;
  private final FileUploadUtil fileUploadUtil;

  @GetMapping("")
  public FeedResponseDTO list(FeedRequestDTO feedRequestDTO) {
    log.info("이건 메인꺼임");
    return userFeedService.getList(feedRequestDTO);
  }

  @GetMapping("/{idx}")
  public FeedDTO detail(@PathVariable("idx") Long idx) {
    return userFeedService.getDetail(idx);
  }

  @PostMapping("")
  public Long insert(FeedDTO feedDTO) {
    return userFeedService.postInsert(feedDTO);
  }


  @GetMapping("/image/{fileName}")
  public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) {
    String menuType = "feed";
    return fileUploadUtil.outputImage(menuType, fileName);
  }
}
