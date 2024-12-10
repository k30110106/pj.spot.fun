package com.spot.fun.usr.feed.dto;

import lombok.*;

import java.util.List;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponseDTO {
  private List<FeedDTO> feedDTOS;
  private boolean hasNext;
  private Long feedCount;
}
