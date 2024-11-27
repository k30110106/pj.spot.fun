package com.spot.fun.usr.feed.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FeedResponseDTO {
    private List<FeedDTO> feedDTOS;
    private boolean hasNext;
}
