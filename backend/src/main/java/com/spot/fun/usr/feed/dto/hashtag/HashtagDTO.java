package com.spot.fun.usr.feed.dto.hashtag;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagDTO {
  private Long idx;
  private String tagName;
  private Boolean delYn;
}
