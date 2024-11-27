package com.spot.fun.usr.feed.dto;

import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  public Feed toEntity() {
    return Feed.builder()
            .content(this.content)
            .delYn(this.delYn)
            .regDate(this.regDate)
            .build();
  }
}
