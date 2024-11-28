package com.spot.fun.usr.feed.dto;

import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDTO {
  private Long idx;
  private String content;
  private Boolean delYn;
  private LocalDateTime regDate;

  private UserDTO userDTO;
  private List<FeedCommentDTO> feedComments;
  private List<FeedImageDTO> feedImages;
  private List<MultipartFile> uploadFiles;

  public Feed toEntity() {
    return Feed.builder()
            .content(this.content)
            .delYn(this.delYn)
            .build();
  }


}
