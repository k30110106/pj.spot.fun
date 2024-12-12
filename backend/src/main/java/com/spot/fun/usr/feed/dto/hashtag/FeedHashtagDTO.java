package com.spot.fun.usr.feed.dto.hashtag;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedHashtagDTO {
  private Long idx;
  private Long feedIdx;
  private Long hashtagIdx;

  private String tagName;
}
